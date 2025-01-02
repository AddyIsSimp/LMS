Download the ZIP file of the project in the code(greenbutton).
Extract the project and open it in IntelliJ if the configuration requests "Load Maven Project" or configure just accept it.
Check the external libraries if contains maven libraries(If not contact me)

ADD JAVAFX SDK IN THE LIBRARY
1) Add the JavaFX SDK you can download it here "https://gluonhq.com/products/javafx/"
2) After extracting the SDK zip you locate in IntelliJ the ProjectStructure>Libraries>PlusSign> then locate the path/to/javafx/lib then add it

SETUP VM Options
1) Go to File >Run >EditConfiguration
2) Add new Application, locate the ModifyOptions then select Add VM Options
3) Insert this in VM option textfield (--module-path "C:path/to/javafx/lib" --add-modules javafx.controls,javafx.fxml) change the path with "" and insert the location of your lib folder
4) Then select the index.java as the main file 

TODO
- Create an executable file .jar
- Borrow transact must not be success if quantity is 0

- Admin - Inventory Table View sort not working
- Student Settings
- InsertBookImageDB - to debug
- Sorting that needs Date sorting
- Reports``
