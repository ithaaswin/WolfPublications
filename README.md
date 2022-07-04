# WolfPublications

# Requirements
1. Java
2. Apache Maven Latest
3. Apache maven's bin path should be included in environment variable "PATH".
4. MariaDB

# Project Narrative
This is the description of the main project for the course Database Management Systems. 
Build a database system, WolfPubDb, for the WolfCity publishing house. The database system will be used by the management and editors of the publishing house, and should maintain information on at least the following: 
* publication: title, editor(s), type (book, journal, magazine), periodicity for periodic publication (weekly, monthly, etc), typical topics (e.g., general, sports, technology, etc), price;
*	edition (1st, 2nd, etc), ISBN number, and publication date for a book;
*	issue of a periodic publication: title, date of issue, articles in the issue;
*	for a book or article: author(s), title, date of creation, text of the article;
*	distributor: name, type (wholesale distributor, bookstore, library), street address, city, phone number, contact person, balance (how much is owed to the publishing house); 
*	order (this is a request for a certain number of copies of a book or of an issue of a periodic publication to be produced by a certain date to meet a distributor's demand): distributor, title of book or issue, number of copies, date, shipping cost.

By talking to the managers and editors, we have elicited the following information about the publishing house
*	The publishing house publishes books, monthly journals, and weekly magazines. Each publication has one or more editors and caters to a certain audience, which is reflected by the selection of, for instance, articles for each issue of a periodic publication.
*	Each issue of each periodic publication is produced on a certain date and consists of a sequence of articles written by journalists. 
*	Both authors and editors get paid for their work, in certain amounts by certain dates. Staff authors and editors get paid periodically; invited editors and authors get paid for the (usually one-time) deliverables of their work.
*	The publishing house sends its production to distributors, which include wholesale distributors, bookstores, and libraries. Distributors place orders for book editions and for issues of publications to be delivered by a certain date. Each distributor has an account with the publishing house; the information in the account includes the distributor's name and contact information, as well as the balance, which is how much the distributor owes the publishing house for outstanding and past orders. 
*	The publishing house keeps information about all current and past orders from each distributor, and bills distributors periodically based on this information.
*	The management of the publishing house collects and analyzes reports on various aspects of the production and distribution of the books, magazines, and journals, as well as of payments to the regular and invited editors and authors. The reports include summaries per month and since the inception of the publishing house, as well as by distributor and location.

## Tasks and Operations
The following are the four major kinds of tasks that are performed using the database. Each task potentially consists of a number of operations; an "operation" is something that corresponds to a separate action. For example, Editing and publishing is considered one task, which involves separate operations such as entering basic information on a new publication, updating information, and assigning editors to publications. 

 
1.	Editing and publishing. Enter basic information on a new publication. Update information. Assign editor(s) to publication. Let each editor view the information on the publications he/she is responsible for. Edit table of contents of a publication, by adding/deleting articles (for periodic publications) or chapters/sections (for books). 
2.	Production of a book edition or of an issue of a publication. Enter a new book edition or new issue of a publication. Update, delete a book edition or publication issue. Enter/update an article or chapter: title, author's name, topic, and date. Enter/update text of an article. Find books and articles by topic, date, author's name. Enter payment for author or editor, and keep track of when each payment was claimed by its addressee. 
3.	Distribution. Enter new distributor; update distributor information; delete a distributor. Input orders from distributors, for a book edition or an issue of a publication per distributor, for a certain date. Bill distributor for an order; change outstanding balance of a distributor on receipt of a payment. 
4.	Reports. Generate montly reports: number and total price of copies of each publication bought per distributor per month; total revenue of the publishing house; total expenses (i.e., shipping costs and salaries). Calculate the total current number of distributors; calculate total revenue (since inception) per city, per distributor, and per location. Calculate total payments to the editors and authors, per time period and per work type (book authorship, article authorship, or editorial work). 

**The detailed description, tasks, reports and other resources are found [here](/Project%20Resources)**

# Steps to run the application

1. Get the code
2. Navigate to "src/main/resources" folder
3. Open "dbconnection.properties" file
4. Update the file according to the database connection.
5. Navigate to Top WolfPublications folder
6. You should be in the same folder level as pom.xml is present
7. Run the command "mvn clean install"
8. Make sure target and lib folders are created after the above command execution


#### For Windows Users:

9. (Optional: Not required if database schema already exists) Run the below command to create Schema in database (from wolf_schema.txt) and populate data (from demoData.txt)
java -cp "target/WolfPublicationsProject-0.0.1-SNAPSHOT-jar-with-dependencies.jar;lib/*" com.ncsu.wolfpub.contents.CreateContents


10. Run the below command to start the Application
java -cp "target/WolfPublicationsProject-0.0.1-SNAPSHOT-jar-with-dependencies.jar;lib/*" com.ncsu.wolfpub.app.App


#### For Mac Users:

9.  (Optional: Not required if database schema already exists) Run the below command to create Schema in database (from wolf_schema.txt) and populate data (from demoData.txt) 
java -cp "target/WolfPublicationsProject-0.0.1-SNAPSHOT-jar-with-dependencies.jar:lib/*" com.ncsu.wolfpub.contents.CreateContents

10. java -cp "target/WolfPublicationsProject-0.0.1-SNAPSHOT-jar-with-dependencies.jar:lib/*" com.ncsu.wolfpub.app.App
