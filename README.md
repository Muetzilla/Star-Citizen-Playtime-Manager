# Star Citizen Playtime Manager

Since Star Citizen has no feature for the player to see the time spent in the game this tool steps in.

## How to use this program

After installing and running the application copy the path to the ``` logbackups ``` folder in your Star Citizen installation i.e. ```  C:\Program Files\Roberts Space Industries\StarCitizen\LIVE\logbackups ```. Then press on the button **Calculate Playtime** and your playtime on the Live server and the PTU will be shown. 

![Star Citizen PlaytimeMAnager GUI Screenshot](https://github.com/Muetzilla/Star-Citizen-Playtime-Manager/blob/main/StarCitizenPlaytimeManagerGUISCreenshot.png)

## How it works
Everytime you play Star Citizen a log file will be created. All those logfiles are then saved inside a folder an can be accessed. 

## What can't be tracked
If you have ever reinstalled Star Citizen the files of all sessions played before the reinstallation can't be tracked since the files are beeing deleted on deleting the game. 

## Roadmap
- [x] Allow selecteion if PTU is installed and Playtime should be tracked
- [x] Export of the Playtime to a JSON file
- [ ] Importing the Playtime JSON File to save Playtime and not rely on the given files (in case the were deleted)
- [ ] A way to visualize the playtime
- [ ] A new UI





