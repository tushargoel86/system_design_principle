# Indexes

### Why Index?
 
* Used by queries to find data from tables quickly. 
* Created on tables and views. 
* Very similar to an index that we find in a book. 
 
<p>
  If we don't have an index in a book and if we need to locate a specific chapter in that book, 
  than we will have to look at every page starting from the first page of the book. 

  On, the other hand, if you have the index, you lookup the page number of the chapter in the index,
  and then directly go to that page number to locate the chapter. 
  
  So book index is helping to drastically reduce the time it takes to find the chapter. 
  
  Similary using the right index improves the performance of queries very much.

</p>

### Index are of several types:
* Clustered Index
* Non Clustered Index
* Unique Constraint Index


Let us take an example of empoyee table without any index;

```bash
CREATE TABLE tblEmployee (
	id INT,
	NAME VARCHAR(50),
	Salary INT,
	Gender VARCHAR(10) 
)


INSERT INTO tblEmployee VALUES (1, 'Tushar Goel', 4500, 'Male');
INSERT INTO tblEmployee VALUES (4, 'TG Goel', 1500, 'Female');
INSERT INTO tblEmployee VALUES (3, 'MM Goel', 6500, 'Female');
INSERT INTO tblEmployee VALUES (2, 'Mohit Goel', 5500, 'Male');

SELECT * FROM tblEmployee;

id	name	     Salary	Gender
1	Tushar Goel	4500	Male
4	TG Goel		1500	Female
3	MM Goel		6500	Female
2	Mohit Goel	5500	Male

```

<p> Data saved in the order we entered. We can use explain to chek the performance </p>

```bash

EXPLAIN (SELECT * FROM tblEmployee WHERE id = 4);

id	select_type	table		type	possible_keys	key	key_len	ref	rows	Extra
1	SIMPLE	tblEmployee	ALL	 \N		\N				\N	  \N		4			Using where
```

We can see here if we need to find a id 4 we need to search all the table. This is called __Table Scan__
As we need to scan all the tables to check the values. In case of large database there would be __performance issue__.
So lets use indexes.

#### 1) Clustered Index:

<p> In mysql, clustered index created by default using Primary key 
or unique key. If you do not define a PRIMARY KEY for your table, MySQL locates the first UNIQUE index where all the
key columns are NOT NULL and InnoDB uses it as the clustered index. 

If the table has no PRIMARY KEY or suitable UNIQUE index, InnoDB internally generates a hidden clustered index 
named GEN_CLUST_INDEX on a synthetic column containing row ID values
</p>


```bash
ALTER TABLE tblEmployee ADD PRIMARY KEY (id)

 SELECT * FROM tblEmployee;
 
id	name		Salary	Gender
1	Tushar Goel	4500	Male
2	Mohit Goel	5500	Male
3	MM Goel		6500	Female
4	TG Goel		1500	Female
```

<p> Now you can see after adding primary key which is clustered index, it stores the Name column as sorted. So, clustered
index stores data in sorted way that's why no duplicate data can be stored without having change in structure. So now
if we check again the performance of the query we see we need not to scan whole table. In single search we able to find value </p>

```bash
id	select_type	table		type	possible_keys	key		key_len	ref		rows	Extra
1	SIMPLE		tblEmployee	const	PRIMARY		PRIMARY		4		const		1	
```




