# BookCat

**An android application for organising a personal book collection**

This Android app was developed as part of my final year studies in a BSc in Computing and Internet Technologies. The aim of the project was to demonstrate my ability to build an app that featured a CRUD system; i.e an application that could create, read, update and delete records in a database. I achieved the project aim by creating 'BookCat', an app designed to allow users to record the details of their personal book collection.

The app was written in Java / XML using Android Studio and also used an API to retrieve a JSON result.

### Key features
- Allows users to store and retrieve details of individual books in a cloud based database
- App uses a CRUD system (create, read, update, delete) to store and manage the books details, including images
- Book details are stored in the cloud using [Google's Firebase](https://firebase.google.com) realtime database
- App uses the [Google Books API](https://developers.google.com/books) to auto complete an entry based on the book's ISBN
- Book details can be manually entered / updated if required

### Main features in action
Create | Read | Update
--- | --- | ---
![Store book](https://github.com/mahmon/visual-timetable-app/blob/master/gifs/create.gif "Create event") | ![View library](https://github.com/mahmon/visual-timetable-app/blob/master/gifs/read.gif "Read event") | ![Update / Delete books](https://github.com/mahmon/visual-timetable-app/blob/master/gifs/update.gif "Update event")

### Google Books API
For extra credit I built the app to populate all the details of the users books automatically, including cover artwork, by taking just one input field - an ISBN number - and using that to query the Google Books API to retrieve the book cover, title and author.

My project received a mark of 70% leading to the award of a first class BSc with honours.

**The Open University, Student ID - mur17004251**
