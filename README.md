# redpill-chat

## Branching Strategy
There are 3 types of branches in the project
* The Main branch called
⋅⋅* develop
* The branch for integrating changes from https://github.com/vector-im/riot-android/tree/master is
** master
** A series of Feature branches that look like 
* CSMC-29_Identify_and_remove_developer_tools

### General RedPill-Chat development is done by 
* creating a feature branch from develop
** use issue_ number_issue_title to name the branch
*** for example CSMC-29_Identify_and_remove_developer_tools
* When Code complete create a Merge request for the develop branch
** assign it to a reviewer
* When review is complete the code is merged into develop 

### Updating RedPill-Chat from Riot-Android
* pull the latest code from Riot-Android into master
* Code Review Changes
* Merge or Cherry Pick changes into Develop

