---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<puml src="diagrams/EditActivityDiagram.puml" width="450" />


### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103T-T09-4/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2425S2-CS2103T-T09-4/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact  with each other for the scenario where the user issues the command `delete_s 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103T-T09-4/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S2-CS2103T-T09-4/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S2-CS2103T-T09-4/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-T09-4/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteStudentCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteStudentCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a student).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `CreateStudentCommandParser`, `DeleteStudentCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

The sequence diagram below illustrates the simplified interactions within the `Logic` component, taking `execute("delete_s 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete_s 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteStudentCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-T09-4/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects and `Cca` objects (which are contained in a `UniquePersonList` object and a `UniqueCcaList` respectively).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the current `Cca` separately in as an unmodifiable `ObservableList<Cca>` just like `Person` objects, however, there is no 'selected' objects.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

#### Cca Related Classes

<puml src="diagrams/CcaInformationClassesDiagram.puml" width="450" />    

The `AddressBook` contains a list of `Cca` objects, and each `Student` references a `CcaInformation` object that contains a unique `Cca`. By storing each CCA only once in the AddressBook and having each Student reference it through a CcaInformation object, it avoids duplicating CCA data across students. The `Addressbook` also ensures that each `CcaInformation` can have a unique `Cca`, preventing duplicate CCAs in the system.

For example, the following object diagrams can be formed:

<puml src="diagrams/CcaInformationObjectDiagram.puml" width="450" />

### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-T09-4/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

<box type="info" seamless>

**Note:** : For simplicity, some details such as conditional checks, parsing and detailed implementation on model changes may have been omitted.

</box>

### **Create Student Feature**

The Add Student feature allows users to add a new student in the address book given a student's `name`, `email`, `phone`, and `address`.

The following shows the activity diagram when the user executes the `create_s` command:

<puml src="diagrams/CreateStudentActivityDiagram.puml" width="450" />

### **Edit CCA Feature**

The Edit CCA feature allows users to edit an existing CCA in the address book with optional parameters (but at least one parameter should be present) for the CCA's `CCA name`, `total Sessions` and set of `Role` objects.

The following shows the activity diagram when the user executes the `edit_c` command:

<puml src="diagrams/EditCcaActivityDiagram.puml" width="450" />

### **Add Role to Student Feature**

The Add role to student feature allows users to add a role to a student in a CCA in the address book.

The following shows the activity diagram when the user executes the `add_r` command:

<puml src="diagrams/AddRoleActivityDiagram.puml" width="450" />

### **Record Attendance to Student Feature**

The record attendance feature allows users to add increment the attendance of a student in a CCA in the address book.

The following shows the activity diagram when the user executes the `attend` command:

<puml src="diagrams/RecordAttendanceActivityDiagram.puml" width="450" />

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user**: Hall attendance managers

**Target user profile**:

* Resolve the distress of the Hall attendance manager caused by the current complex and unorganized CCA attendance system.
* Provide a simple and easy-to-use software that effectively tracks all CCA attendances for hall students.
* Accommodate the manager’s “lazy” nature by streamlining workflows and reducing complexity.
* Prioritize typing over mouse usage to align with the manager’s preferences.

**Value proposition**:  It provides a centralized tracking system for the CCA attendance for hall students in different CCAs, which is required for the point calculation. Students are grouped by CCAs, and each student has their total attended sessions for each CCA. CLI commands allow easy attendance recording and management of students and CCAs.

### User stories

- Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`.
- HAM below refers to Hall Attendance Managers.

| Index | Priority | As a …                             | I can …                                                             | So that I can…                                                                           |
|-----: | :------: | :---------------------------------- |:--------------------------------------------------------------------|:-----------------------------------------------------------------------------------------|
| 1     | `* * *`  | HAM                                 | create new students in the student list                             | store their information.                                                                 |
| 2     | `* * *`  | HAM                                 | delete existing students from the student list                      | remove ex-students and other redundant data.                                             |
| 3     | `* *`    | HAM                                 | filter student list by name                                         | find students quickly.                                                            |
| 4     | `* * *`  | HAM                                 | create a CCA role                                                   | label students according to specific responsibilities in that CCA.                       |
| 5     | `* * *`  | HAM                                 | delete an existing CCA role                                         | remove any outdated roles from a CCA                             |
| 6     | `* * *`  | HAM                                 | add roles to a student in a CCA                                     | categorize students by their responsibilities (e.g. captain, exco, non-official member). |
| 7     | `* * *`  | HAM                                 | remove a role from a student in a CCA                               | remove any outdated roles (e.g. if the student resigns from a role).                    |
| 8    | `* *`    | HAM                                 | edit student profiles                                               | keep their information up to date and recover from mistakes (e.g. a change of address).  |
| 9    | `* *`    | HAM                                 | edit CCA profiles                                                   | keep CCA information up to date and recover from mistakes (e.g. update total sessions)  |
| 10    | `*`      | new HAM                             | use the “help” command                                              | learn how to use the application.                                                        |
| 11    | `* * *`  | potential HAM exploring the app      | see the application populated with sample student information       | understand how it looks in active use.                                                   |
| 12    | `* * *`  | new HAM ready to start using the app | purge all current data                                              | remove any sample student information used for testing.                                  |
| 13    | `*`      | HAM taking over from another HAM     | import student profiles                                             | transfer existing student information into my system.                                    |
| 14    | `*`      | HAM taking over from another HAM     | export student profiles                                             | pass the current student information on to another HAM.                                  |
| 15    | `* * *`  | HAM                                 | record a student’s attendance in a CCA                              | accurately record students' attendance of CCA sessions                                                             |
| 16    | `* * *`  | HAM                                 | see the number of sessions each student has attended                | accurately track students' attendance                                                             |
| 17    | `* *`    | HAM                                 | set a maximum attendance for CCA                                    | have a better idea of the student’s attendance rate.                                     |
| 18    | `* * *`  | HAM                                 | create a new CCA                                                    | assign it to students.                                                                   |
| 19    | `* * *`  | HAM                                 | delete a CCA                                                        | remove any CCA that are no longer active or needed.                                          |
| 20    | `* * *`  | HAM                                 | add a CCA to a student                                              | keep their records accurate.                                                             |
| 21    | `* * *`  | HAM                                 | delete a CCA from a student                                         | update their status if they leave or switch CCAs.                                        |
| 22    | `* * *`  | HAM                                 | view the list of CCAs separately from the student list              | see all CCAs at once.                                                                    |
| 23    | `* *`    | HAM                                 | see the list of roles in a CCA                                      | know which roles can be assigned to students.                                            |
| 24   | `* *`    | HAM                                 | have automatic validation of CCA names when adding CCA to student   | avoid errors when adding CCA to students.                                                  |
| 25    | `* *`    | HAM                                 | have automatic validation of role names when adding role to student | avoid errors when adding roles to students.                                               |
| 26    | `* *`    | HAM                                 | save my changes when I exit the application                         | avoid losing important changes                                                                  |
| 27   | `* *`    | HAM                                 | see the list of all students in the system                          | know who is in the system.                                                                |
| 28   | `* *`    | HAM                                 | see the list of all CCAs in the system                              | know which CCAs are in the system.                                                        |
| 29   | `* *`    | HAM                                 | see the list of roles in each CCA                                   | know which roles each CCA has.                                                        |
| 30   | `* *`    | HAM                                 | record attendance for more than one session at a time               | save time when recording attendance. |



### Use cases

(For all use cases below, the **System** is `CCAttendance`, the person is `Hall Attendance Manager`, and the **Actor** is the `user`, unless specified otherwise).

**UC1: List students**

**MSS**

1. User requests to list students.
2. System shows a list of all students.

    Use case ends.

**Extensions**
* 1a. The student list is empty.

  Use case ends.

**UC2: Create a student**

**MSS**

1.  User <u>lists the students (UC1)</u>.
2. User requests to create a student with the corresponding details.
3. System creates the student and adds it to the student list.
4. System shows the student has been added.

    Use case ends.

**Extensions**

* 2a. The student already exists in the student list.

    * 2a1. System shows an error message.

      Use case resumes at step 2.

**UC3: Delete a student**

**MSS**

1. User <u>lists the students (UC1)</u>.
2. User requests to delete a specific student.
3. System deletes the student from the student list.
4. System shows the student has been deleted.

    Use case ends.

**Extensions**

* 2a. The student does not exist in the student list.

    * 2a1. System shows an error message.

      Use case resumes at step 2.

**UC4: Edit a student**

**MSS**

1. User <u>lists the students (UC1)</u>.
2. User requests to edit a student's information from the list.
3. System edits the student from the list.
4. System shows the student has been edited.

    Use case ends.

**Extensions**

* 2a. The student does not exist in the student list.

    * 2a1. System shows an error message.

      Use case resumes at step 2.

* 2b. The parameters to edit does not exist.

  * 2b1. System shows an error message.

    Use case resumes at step 2.

* 2c. The parameters to edit is invalid.

    * 2c1. System shows an error message.

      Use case resumes at step 2.

**UC5: Find a student**

**MSS**

1. User requests to find students with specific keywords in a name.
2. System searches for students matching the keywords.
3. System shows a list of searched students.

    Use case ends.

**Extensions**

* 1a. No students match the keywords.
    Use case ends.

* 1b. The student list is empty.
    Use case ends.

* 1c. The keyword is an invalid format or missing keywords.

    * 1c1. System shows an error message.

        Use case resumes at step 1.

**UC6: Create a CCA**

**MSS**

1.  User requests to create a CCA with the corresponding details.
2.  System creates the CCA and adds it to the CCA list.
3.  System shows the CCA has been added.

    Use case ends.

**Extensions**

* 1a. The CCA already exists in the CCA list.

    * 1a1. System shows an error message.

      Use case resumes at step 1.

**UC7: Delete a CCA**

**MSS**

1. System shows list of CCAs.
2. User requests to delete a specific CCA.
3. System deletes the CCA from the list.
4. System shows the CCA has been deleted.

    Use case ends.

**Extensions**

* 2a. The CCA does not exist in the list.

    * 2a1. System shows an error message.

      Use case resumes at step 2.

**UC8: Edit a cca**

**MSS**

1. System shows list of CCAs.
2. User requests to edit a CCA's information from the list.
3. System edits the CCA from the list.
4. System shows the CCA has been edited.

   Use case ends.

**Extensions**

* 2a. The CCA does not exist in the CCA list.

    * 2a1. System shows an error message.

      Use case resumes at step 2.

* 2b. The parameters to edit does not exist.

    * 2b1. System shows an error message.

      Use case resumes at step 2.

* 2c. The parameters to edit is invalid.

    * 2c1. System shows an error message.

      Use case resumes at step 2.

**UC9: Add a role to a student in a CCA**

**MSS**

1.  User <u>lists the students (UC1)</u>.
2. System shows list of CCAs.
3. User requests to add a role to a specific student in a specific CCA.
4. System adds the role to the student in the CCA.
5. System shows the role has been added.

    Use case ends.

**Extensions**

* 3a. The student does not exist in the student list.

    * 3a1. System shows an error message.

      Use case resumes at step 3.

* 3b. The CCA does not exist in the CCA list.

    * 3b1. System shows an error message.

      Use case resumes at step 3.

* 3c. The student already has a role in the CCA.

    * 3c1. System shows student already has role in the CCA.

      Use case resumes at step 3.

* 3d. The student is not in the CCA.

    * 3d1. System shows an error message.

      Use case resumes at step 3.

* 3e. The role does not exist.

    * 3e1. System shows an error message.

      Use case resumes at step 3.

**UC10: Remove a role from a student in a CCA**

**MSS**

1.  User <u>lists the students (UC1)</u>.
2.  User requests to remove a role from a specific student in a specific CCA.
3. System removes the role from the student in the CCA.
4. System shows the role has been removed.

    Use case ends.

**Extensions**

* 2a. The student does not exist in the student list.

    * 2a1. System shows an error message.

      Use case resumes at step 2.

* 2b. The CCA does not exist in the CCA list.

    * 2b1. System shows an error message.

      Use case resumes at step 2.

* 2c. The student is not in the CCA.

    * 2c1. System shows an error message.

      Use case resumes at step 2.

* 2d. The student does not have the role in the CCA.

    * 2d1. System shows an error message.

      Use case resumes at step 2.

**UC11: Add CCA to a student**

**MSS**

1.  User <u>requests to list the students (UC1)</u>.
2. System shows list of CCAs.
3. User requests to add a CCA to a specific student.
4. System adds the CCA to the student.
5. System shows the CCA has been added.

    Use case ends.

**Extensions**

* 2a. The CCA list is empty.

   Use case ends.

* 3a. The student does not exist in the student list.

    * 3a1. System shows an error message.

      Use case resumes at step 3.

* 3b. The CCA does not exist in the CCA list.

    * 3b1. System shows an error message.

      Use case resumes at step 3.

* 3c. The student is already in the CCA.

    * 3c1. System shows student already in CCA.

      Use case resumes at step 3.

**UC12: Delete a CCA from a student**

**MSS**

1.  User <u>lists the students (UC1)</u>.
2. User requests to delete a CCA from a specific student.
3. System deletes the CCA from the student.
4. System shows the CCA has been deleted.

    Use case ends.

**Extensions**

* 2a. The student does not exist in the student list.

   * 2a1. System shows an error message.

      Use case resumes at step 2.

* 2b. The CCA does not exist in the CCA list.

   * 2b1. System shows an error message.

      Use case resumes at step 2.

* 2c. The student is not in the CCA.

   * 2c1. System shows an error message.

      Use case resumes at step 2.

**UC13: Clear all students**

**MSS**

1. User requests to clear all the students.
2. System clears all the students.
3. System shows all the students have been cleared.

    Use case ends.

**UC14: Record a student's attendance**

**MSS**
1.  User <u>lists the students (UC1)</u>.
2. User requests to record a certain attendance amount for a specific student and a given CCA.
3. System increments the student’s attendance in that CCA by the requested amount.
4. System shows that the attendance has been updated successfully.

    Use case ends.

**Extensions**

* 2a. The student does not exist in the student list.

    * 2a1. System shows an error message.

      Use case resumes at step 2.

* 2b. The CCA does not exist in the CCA list.

    * 2b1. System shows an error message.

      Use case resumes at step 2.

* 2c. The student is not in the specified CCA.

    * 2c1. System shows an error message.

      Use case resumes at step 2.

* 2d. The attendance amount is missing or invalid.

    * 2d1. System shows an error message.

      Use case resumes at step 2.

### UC15: Exit the application

**MSS**
1. User requests to exit the application.
2. System saves all the data and closes the application.

    Use case ends.

### Non-Functional Requirements

1. **Platform Compatibility**: The application should work on any _mainstream OS_ (Windows, Linux, macOS) as long as Java `17` or above is installed. *(Constraint-Platform-Independent, Constraint-Java-Version)*

2. **Performance**: The application should be able to hold up to **1000 students** without noticeable sluggishness in performance for typical usage. *(Scalability requirement)*

3. **Typing Efficiency**: A user with above-average typing speed for regular English text (i.e., not code, not system admin commands) should be able to accomplish most tasks **faster using commands** than using the mouse. *(Constraint-Typing-Preferred, Recommendation-CLI-First)*

4. **Incremental Development**: The software should be developed in **small, incremental updates** throughout the project lifecycle instead of being built in one go. *(Constraint-Incremental)*

5. **Storage Format**: Data should be stored in a **human-editable text file** (e.g., JSON, CSV) rather than a database or proprietary format. *(Constraint-Human-Editable-File, Constraint-No-DBMS)*

6. **Portability**: The application should **not require an installer** and should be packaged as a **single JAR file**. *(Constraint-Portable, Constraint-Single-File)*

7. **No Remote Server Dependency**: The application should be **fully functional without requiring an internet connection** or an external server. *(Constraint-No-Remote-Server, Recommendation-Minimal-Network)*

8. **Screen Resolution Support**: The GUI should work well on **standard screen resolutions** (1920×1080 and higher) and be usable at **lower resolutions** (1280×720). *(Constraint-Screen-Resolution)*

9. **Security & Privacy**: User data should be stored **locally** and should **not be accessible to other users** during regular operations. *(Constraint-Single-User)*

10. **Extensibility & Maintainability**: The application should follow **Object-Oriented Programming (OOP) principles**, making it **easy to extend and modify**. *(Constraint-OO)*

*{More to be added as needed}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **CLI**: Command Line Interface, a text-based interface that allows users to interact with the system by typing commands
* **GUI**: Graphical User Interface, a visual interface that allows users to interact with the system using graphical elements such as CLI, buttons, etc.
* **Index**: A number that represents the position of student or CCA. It is shown in the UI on the left side of the student or CCA details
* **HAM**: Hall attendance manager, i.e. the one in charge of taking attendance of the students
* **Role**: The position the student has in the CCA, e.g. Captain, President

*{More to be added}*

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more \*exploratory\* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

   1. If this doesn't work, open a terminal in the folder and run the command `java -jar CCAttendance.jar`.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.
   
   1. Refer to the initial launch instructions if double-clicking doesn't work

### Listing a student

1. List all students
    1. Test case: `list`
       Expected: The list should contain all students. The status message should reflect the successful listing of all students.

### Clearing all students

1. Clear all students
    1. Prerequisites: List all students using the `list` command. Multiple students in the list.
    2. Test case: `clear`
       Expected: Both the student list and the CCA list should be empty. The status message should reflect the successful clearing of all students.

### Finding a student

1. Find a student by querying name
    1. Prerequisites: For this test, you will use the sample data provided when the app is launched the first time. Make sure the data file is not corrupted.
    2. Test case: `find Alex`
       Expected: The list should contain one student `Alex Yeoh` only.

### Creating a student

1. Creating a student
    1. Prerequisites: List all students using the `list` command. Multiple students in the list. `John Doe` is not in student list.
    2. Test case: `create_s n/John Doe p/98765432 e/e0000000@u.nus.edu a/Raffles Hall 22/B/2`
       Expected: A new student is added to the list. The student details are shown in the list.
    3. Test case: `create_s n/John Doe p/98765432`
       Expected: Error message is shown as insufficient parameters are provided. The student is not added to the list.
    4. Test case: `create_s n/John Doe p/98765432 e/e0000000@u.nus.edu a/Raffles Hall 22/B/2`
       Expected: Error message is shown as the student already exists in the list. The student is not added to the list.
    5. Other incorrect create student commands to try: `create_s n/helloworld* p/helloworld* e/helloworld* a/helloworld*` (incorrect inputs, missing inputs, or incorrect prefixes used)
       Expected: Error message is shown as parameters with invalid formats were provided. The student is not added to the list.

### Deleting a student

1. Deleting a student while all students are being shown
    1. Prerequisites: List all students using the `list` command. Multiple students in the list.
    2. Test case: `delete_s 1`
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message.
    3. Test case: `delete_s 0`
       Expected: No student is deleted. Error details shown in the status message.
    4. Other incorrect delete commands to try: `delete_s`, `delete_s x`, `...` (where x is larger than the list size)
       Expected: Similar to previous.

### Creating a CCA

1. Creating a CCA with a CCA name.
    1. Prerequisites: List all CCAs using the `list` command. Multiple CCAs in the list. `Handball` is not in CCA list.
    2. Test case: `create_c c/Handball`
       Expected: A new CCA is added to the list. The CCA details are shown in the list.
    3. Test case: `create_c`
       Expected: No CCA is added. Error details shown in the status message as the CCA name is not provided.
    4. Test case: `create_c c/Handball`
       Expected: No CCA is added. Error details shown in the status message as the CCA already exists.
    5. Other test cases to try: `create_c c/helloworld*` (incorrect inputs, missing inputs, or incorrect prefixes used)
       Expected: No CCA is added. Error message is shown as parameters with invalid formats were provided.

### Deleting a CCA

1. Deleting a CCA while all CCAs are being shown
    1. Prerequisites: List all CCAs using the `list` command. One CCA in the list.
    2. Test case: `delete_c 1`
       Expected: The first CCA is deleted from the list. Details of the deleted CCA shown in the status message.
    3. Test case: `delete_c 1`
       Expected: There are no more CCAs in the list. Error details are shown in the status message.
    4. Test case: `delete_c 0`
       Expected: No CCA is deleted. Error details shown in the status message.
    5. Other incorrect delete commands to try: `delete_c`, `delete_c x`, `...` (where x is larger than the list size)
       Expected: Similar to previous.

### Editing a student

1. Editing an existing student's name, phone, email and address.
    1. Prerequisites: List all students using the `list` command. Multiple students in the list and `Clark Kent` is not in the list. It is okay for the edited student's original name to be Clark Kent
    2. Test case: `edit_s 1 n/Clark Kent p/99999999 e/e0000000@u.nus.edu a/Raffles Hall 22/B/2`
       Expected: The first student's name is changed to `Clark Kent`. The updated student details are shown in the list.
    3. Test case: `edit_s 2 n/Clark Kent`
       Expected: No student is edited. Error details are shown in the status message as Clark Kent already exists in the list.
    4. Test case: `edit_s 0 n/Josh Yoseph`
       Expected: No student is edited. Error details shown in the status message as the index is out of the student list.
    5. Other test cases to try: `edit_s 1 n/helloworld* p/helloworld* e/helloworld* a/helloworld*` (incorrect inputs, missing inputs, or incorrect prefixes used)
       Expected: No student is edited. Error message is shown as parameters with invalid formats were provided.
2. Editing CCA of a student will be handled in separate test cases below.

### Editing a CCA

1. Editing an existing CCA's name and total sessions.
    1. Prerequisites: List all CCAs using the `list` command. Multiple CCAs in the list and there is no `Basketball` CCA. It is okay for the edited CCA's original name to be `Basketball`
    2. Test case: `edit_c 1 c/Basketball t/15 r/President r/Vice-President r/Treasurer`
       Expected: The first CCA's name is changed to `Basketball`. The respective details are changed as specified. The updated CCA details are shown in the list.
    3. Test case: `edit_c 2 c/Basketball`
       Expected: No CCA is edited as the CCA already has the name `Basketball`. Error details are shown in the status message as the CCA already exists in the list.
    4. Test case: `edit_c 0 c/Chess`
       Expected: No CCA is edited. Error details shown in the status message as the index is out of the CCA list.
    5. Other test cases to try: `edit_c 1 c/helloworld*` (incorrect inputs, missing inputs, or incorrect prefixes used, wrong index used)
       Expected: No CCA is edited. Error message is shown as parameters with invalid formats were provided.

2. Editing a role of a CCA is handled in separate test cases below.

### Editing a role of a CCA

1. Adding a role to a CCA
    1. Prerequisites: List all CCAs using the `list` command. Multiple CCAs in the list.
    2. Test case: `edit_c 1 r/President r/Vice-President r/Treasurer`
       Expected: The first CCA's roles are replaced with `President`, `Vice-President`, `Treasurer`, and default `Member` roles. The updated CCA details are shown in the list.
    3. Test case: `edit_c 1 r/President`
       Expected: The first CCA's roles are replaced with the `President` and default `Member` role. Other roles get deleted. The updated CCA details are shown in the list.
    4. Test case: `edit_c 0 r/Treasurer`
       Expected: No CCA is edited. Error details shown in the status message as the index is out of the CCA list.
    5. Other test cases to try: `edit_c 1 r/helloworld*` (incorrect inputs, missing inputs, or incorrect prefixes used)
       Expected: No CCA is edited. Error message is shown as parameters with invalid formats were provided.

2. Deleting a role from a CCA
    1. Prerequisite: List all CCAs using the `list` command. Multiple CCAs in the list. Multiple roles in the list and the first CCA has at least one role.
    2. Test case: `edit_c 1 r/`
       Expected: The first CCA's roles are removed and only left with default `Member`. The updated CCA details are shown in the list.
    3. Test case: `edit_c 0 r/`
       Expected: No CCA is edited. Error details shown in the status message as the index is out of the CCA list. Status bar remains the same.

### Record Attendance

1. Recording attendance for a student already in a CCA
    1. Prerequisite: Multiple students in the list (e.g., from previous sample data). The second student (index `2`) is already in the `Basketball` CCA. The `Chess` CCA does not exist and `Dance` CCA is not contained in the first student. The second student does not have full attendance in `Basketball` CCA.
    2. Test case: `attend 2 c/Basketball a/1`
   Expected: The second student’s attendance for the `Basketball` CCA is incremented by 1.  A success message is displayed, indicating that attendance has been updated.
   3. Test case: `attend 2 c/Chess a/1`
   Expected: No attendance is recorded. An error message is displayed indicating that the `Chess` does not exist).
   4. Test case: `attend 2 c/Dance a/1`
    Expected: No attendance is recorded. An error message is displayed indicating that the student is not in the `Dance` CCA.
   5. Test case: `attend 0 c/Basketball a/1`
    Expected: No attendance is recorded. An error message is shown, since `0` is out of the valid student index range.
   6. Test case: `attend 2 c/Basketball`
   Expected: No attendance is recorded. An error message is displayed, indicating missing or invalid parameters (e.g., `a/AMOUNT`).
   7. Other incorrect attendance commands to try: `attend`, `attend x`, `attend 2`, `attend 2 c/Basketball a/abc` (where x is larger than the list size).
    Expected: Similar to previous.

### Adding a CCA to a student

1. Adding a CCA to a student.
    1. Prerequisites: List all students using the `list` command. Multiple students in the list. Multiple CCAs in the list and it has `Basketball` and `Acting`. It does not contain `Chess`. The first student is not in `Basketball` CCA.
    2. Test case: `add_c 1 c/Basketball`
       Expected: The first student is added to the `Basketball` CCA. The updated student details are shown in the list.
    3. Test case: `add_c 1 c/Basketball`
       Expected: The first student is already in the `Basketball` CCA. Error details shown in the status message.
    4. Test case: `add_c 1 c/Chess`
       Expected: The app does not have the `Chess` CCA. Error details shown in the status message.
    5. Test case: `add_c 0 c/Basketball`
       Expected: No student is edited. Error details shown in the status message as the index is out of the student list.
    6. Other test cases to try: `add_c 1 c/helloworld*` (incorrect inputs, missing inputs, or incorrect prefixes used)
       Expected: No student is edited. Error message is shown as parameters with invalid formats were provided.

### Removing a CCA from a student

1. Removing a CCA from a student.
    1. Prerequisite: List all students using the `list` command. Multiple students in the list. Multiple CCAs in the list and it has `Basketball` and `Acting`. It does not contain `Chess`. The first student is in `Basketball` CCA.
    2. Test case: `remove_c 1 c/Basketball`
       Expected: The first student is removed from the `Basketball` CCA. The updated student details are shown in the list.
    3. Test case: `remove_c 1 c/Basketball`
       Expected: The first student is already not in any CCA. Error details shown in the status message.
    4. Test case: `remove_c 0 c/Basketball`
       Expected: No student is edited. Error details shown in the status message as the index is out of the student list.

### Adding a role to a student in a CCA

1. Adding a role from a student in a CCA.
    1. Prerequisite: List all students using the `list` command. Multiple students in the list. Multiple CCAs in the list and the first person is in a CCA `Basketball`. The `Basketball` CCA has `Captain` role defined. The first person in `Basketball` and assigned with a default role `Member`.
    2. Test case: `add_r 1 c/Basketball r/Member`
      Expected: The default role `Member` cannot be assigned. Error details are shown in the status message.
    3. Test case: `add_r 1 c/Basketball r/Captain`
      Expected: The first person is added with the role. The updated student details are shown in the list.
    4. Test case: `add_r 1 c/Basketball r/Captain`
      Expected: The first person is already assigned with a role. Error details are shown in the status message.

### Removing a role from a student in a CCA

1. Removing a role from a student in a CCA.
    1. Prerequisite: List all students using the `list` command. Multiple students in the list. Multiple CCAs in the list and the first person has at least one CCA. The person must be assigned with a role other than `Member`.
    2. Test case: `remove_r 1 c/Basketball`
       Expected: The first student's role in `Basketball` CCA is removed, the student is now a `Member`. The updated student details are shown in the list.
    3. Test case: `remove_r 1 c/Basketball`
       Expected: The first student is not assigned with any role. Error details shown in the status message.
    4. Test case: `remove_r 0 c/Basketball`
       Expected: No role is edited. Error details shown in the status message as the index is out of the student list.

### Saving data

1. Dealing with missing/corrupted data files
    1. Prerequisite: For this test, you will use the sample data provided when the app is launched the first time. Make sure the data file is not corrupted.
    2. Test case: Delete the data file.
       Under the app folder, go to the data folder, and delete the `addressbook.json` file.
       Expected: The app should start with the sample data when launched. Upon any action with storage (e.g. adding a student), a new data file should be created.
    3. Test case: Corrupt the data file.
       Open the `addressbook.json` file in a text editor and delete the first five lines.
       Expected: The app should start with an empty data when launched. Upon any action with storage (e.g. adding a student), a new data file should be created.
    4. Test case: Corrupt the data file with an extra parameter.
       Open the `addressbook.json` file in a text editor and add `    "name" : "Alice Paul",` in the third line.
       Expected: The app should start with the sample data when launched. However, the first student's name is `Alice Paul` instead of `Alex Yeoh`.
    5. Test case: Corrupt the data file with an invalid parameter.
       Open the `addressbook.json` file in a text editor and edit `    "name" : "Alex Yeoh",` to `    "name" : "Alex !!!",` in the third line.
       Expected: The app should start with an empty data when launched. Upon any action with storage (e.g. adding a student), a new data file should be created.
