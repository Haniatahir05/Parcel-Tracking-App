## Parcel Tracking Application
A Java-based management system designed to streamline the tracking and logistics of shipments. This application allows users to monitor the journey of a package from dispatch to delivery, ensuring transparency and efficiency in the delivery process.
## Core Features
* Package Registration: Add new parcels with unique tracking IDs, sender/receiver details, and destination addresses.
* Real-Time Status Updates: Update and view the current status of a shipment (e.g., Pending, In Transit, Out for Delivery, Delivered).
* Search Functionality: Instantly retrieve parcel details using a unique tracking number.
* Data Persistence: Efficient handling of parcel records within the application logic.
## Technical Highlights
The application is built using Java and focuses on clean, modular code. Key technical aspects include:
* Object-Oriented Design: Utilization of a Parcel class to encapsulate data like tracking numbers and status.
* Collection Framework: Likely uses ArrayList or HashMap for efficient storage and retrieval of parcel records.
* Input Validation: Ensures that tracking IDs are formatted correctly and that required fields are not left empty.
## Installation & Setup
### Prerequisites
* Java Development Kit (JDK): Version 8 or higher.
* Git: For cloning the repository.
### Quick Start
1. Clone the project:
```Bash```
git clone https://github.com/Haniatahir05/Parcel-Tracking-App.git
```Bash```
2. Navigate to the source folder:
```Bash```
cd Parcel-Tracking-App
```Bash```
3. Compile the source code:
```Bash```
javac ParcelTrackingApp.java
```Bash```
4. Launch the application:
```Bash```
java ParcelTrackingApp
```Bash```
## User Guide
* Adding a Parcel: Follow the prompts to enter the weight, destination, and recipient info.
* Tracking: Enter your assigned tracking ID to see exactly where your package is in the system.
* Admin View: (If applicable) Update the status of multiple parcels as they move through different checkpoints.

## Future Roadmap
* Adding a database backend (like SQLite) for permanent data storage.
* Implementation of a Google Maps API for visual location tracking.
* Email notifications for status changes.
