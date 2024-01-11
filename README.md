# Star Citizen Playtime Manager

Since Star Citizen has no feature for the player to see the time spent in the game this tool steps in.

## How to use this program

After installing and running the application copy the path to the ``` logbackups ``` folder in your Star Citizen installation i.e. ```  C:\Program Files\Roberts Space Industries\StarCitizen\LIVE\logbackups ```. Then press on the button **Calculate Playtime** and your playtime on the Live server and the PTU will be shown. 

![Star Citizen PlaytimeMAnager GUI Screenshot](https://github.com/Muetzilla/Star-Citizen-Playtime-Manager/blob/main/StarCitizenPlaytimeManagerGUISCreenshot.png)

## How it works
Everytime you play Star Citizen a log file will be created. All those logfiles are then saved inside a folder an can be accessed. 

## What can't be tracked
If you have ever reinstalled Star Citizen the files of all sessions played before the reinstallation can't be tracked since the files are beeing deleted on deleting the game. 

## What's new?
- A new button that lets you select whether you have playtime on the PTU servers that you would like to keep track of or not.
- A basic export of your playtime on the Live server into a json file

## What's next

I'm currently working on a function to export the playtime into a seperate file. This will allow to save the playtime if the log files are beeing deleted for some reason.  
