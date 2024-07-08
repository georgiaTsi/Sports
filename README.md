# Sports App #

Sports App is an Android application that allows users to browse upcoming sports events and mark their favorite sports and events. 
The app fetches event data from an external API and provides a user-friendly interface to explore and track events.
User preferences, such as favorite sports and events, are stored locally using SQLite.

# Features #
Browse Events: View a list of upcoming sports events, categorized by sport.
Favorite Sports: Mark sports as favorites to easily access their events.
Favorite Events: Mark individual events as favorites for quick reference.
Event Details: View detailed information about each event, including competitors and time.
Filtering: Filter the list of sports to show only favorites.
Expanding/Collapsing Sports: Expand or collapse sport categories to show or hide their events.

# Architecture #
The app follows the Model-View-ViewModel (MVVM) architectural pattern, ensuring a clear separation of concerns:
Model: Represents the data (sports, events) and handles data access (API calls, database interactions).
View: Responsible for displaying the UI and handling user interactions.
ViewModel: Acts as an intermediary between the View and the Model, providing data to the View and handling user actions.

# Technologies Used #
Kotlin: The primary programming language for the app.
Android SDK: The framework for building Android applications.
Jetpack Components:
 - ViewModel: Manages UI-related data and handles user actions.
 - RecyclerView: Efficiently displays lists of items.
 - Room: Provides an abstraction layer over SQLite for local data persistence.
 - Retrofit: A type-safe HTTP client for making API calls.
 - Mockito: A mocking framework for unit testing.

# Unit Tests #
The repository includes unit tests for the EventAdapter and SportAdapter classes.
Run the tests to ensure the functionality of these components.
  
Clone the Repository: git clone https://github.com/georgiaTsi/Kaizen