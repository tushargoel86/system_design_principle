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
Data saved in the order we entered.
