# Indexes

### Why Index?
 
* Used by queries to find data from tables quickly. 
* Created on tables and views. 
* Very similar to an index that we find in a book. 
 
<p>
  If we don't have an index in a book and if we need to locate a specific chapter in that book, 
  than we will have to look at every page starting from the first page of the book. 

  On, the other hand, if you have the index, you lookup the page number of the chapter in the index,
  and then directly go to that page number to locate the chapter. So book index is helping to drastically
  reduce the time it takes to find the chapter. 
  
  Similary using the right index improves the performance of queries very much.

</p>

### Index are of several types:
* Clustered Index
* Non Clustered Index
* Unique Constraint Index


Let us take an example of __empoyee__ table without any index;

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

  Data saved in the order we entered. We can use __explain__ command to chek the performance  

```bash

EXPLAIN (SELECT * FROM tblEmployee WHERE id = 4);

id	select_type	table		type	possible_keys	key	  key_len	ref	  rows	Extra
1	SIMPLE		tblEmployee	ALL		 \N				\N	   \N	    \N		4	 Using where
```

We can see here if we need to find a id 4 we need to search all the table. This is called __Table Scan__
as we need to scan all the table to find the value. In case of large database there would be __performance issue__ due
to table scan. So lets use indexes than.

#### 1) Clustered Index:

<p> In mysql, clustered index created by default using Primary key 
or unique key. If you do not define a PRIMARY KEY for your table, MySQL locates the first UNIQUE index where all the
key columns are NOT NULL and InnoDB uses it as the clustered index. 

If the table has no PRIMARY KEY or suitable UNIQUE index, InnoDB internally generates a hidden clustered index 
named GEN_CLUST_INDEX on a synthetic column containing row ID values.

We can have only have 1 clustered index whether primary key or composite key index. To use 
different clustered index we need to delete older one.
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
index stores data in sorted way that's why no duplicate data can be stored without having change in structure.

So now if we check again the performance of the query we see we need not to scan whole table. In single search we able
to find value </p>

```bash
id	select_type	table		type	possible_keys	key		key_len	ref		rows	Extra
1	SIMPLE		tblEmployee	const	PRIMARY		PRIMARY		4		const	 1	
```


#### 2) Non Clustered Index:
<p>
 A non-clustered index (or regular b-tree index) is an index where the order of the rows does not match the physical order of the actual data.  
 It is instead ordered by the columns that make up the index. 
 In a non-clustered index, the leaf pages of the index do not contain any actual data, but instead contain pointers to the actual data. 
 These pointers would point to the clustered index data page where the actual data exists (or the heap page if no clustered index exists on the table).
</p>

```bash
 CREATE INDEX IX_tblEmployee_Name ON tblEmployee(NAME)

 SELECT * FROM tblEmployee;
 
id	name		Salary	Gender                      name    	 Row Address
1	Tushar Goel	4500	Male					Tushar Goel      Row Address
2	Mohit Goel	5500	Male					Mohit Goel		 Row Address
3	MM Goel		6500	Female					MM Goel			 Row Address
4	TG Goel		1500	Female					TG Goel			 Row Address
```

Since Non clustered index are stored seprately from the actual data, a table can have more than one non clsutered index.

#### 3) Unique Index:

Unique index is used to enforce uniqueness of key values in the index. Let's understand this with an example. 

We can verify this by;

```bash
ALTER TABLE tblEmployee ADD PRIMARY KEY (id)

SHOW INDEX FROM tblEmployee

Table		Non_unique	Key_name	Seq_in_index	Column_name	Collation	Cardinality	Sub_part	Packed	Null	Index_type	Comment
tblEmployee	0			PRIMARY			1			id				A		2				\N		\N				BTREE	
```

UNIQUENESS is a property of an Index, and both CLUSTERED and NON-CLUSTERED indexes can be UNIQUE.

__Note:__
* By default, a PRIMARY KEY constraint, creates a unique clustered index, where as a UNIQUE constraint creates a unique nonclustered 
index. These defaults can be changed if you wish to.

* A UNIQUE constraint or a UNIQUE index cannot be created on an existing table, if the table contains duplicate values in the key columns.
 Obviously, to solve this,remove the key columns from the index definition or delete or update the duplicate values

 
__Diadvantages of Indexes:__

__Additional Disk Space:__ 
Clustered Index does not, require any additional storage. Every Non-Clustered index requires additional space as it is 
stored separately from the table.The amount of space required will depend on the size of the table, and the number and types of columns used in the index.

__Insert Update and Delete statements can become slow:__ 
When DML (Data Manipulation Language) statements (INSERT, UPDATE, DELETE) modifies data in a table,
the data in all the indexes also needs to be updated. Indexes can help, to search and locate the rows, that we want to delete, but too many indexes to update can actually hurt the performance of data modifications.

__What is a covering query?__
If all the columns that you have requested in the SELECT clause of query, are present in the index, then there is no need to lookup in the table again. The requested columns data can simply be returned from the index.


A clustered index, always covers a query, since it contains all of the data in a table. A composite index is an index on two or more columns. Both clustered and nonclustered indexes can be composite indexes. To a certain extent, a composite index, can cover a query.
