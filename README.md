# redpill-chat

## Branching Strategy
There are 3 types of branches in the project
- A single Main branch called __develop__
- A single branch for integrating changes from [Riot-Android/master](https://github.com/vector-im/riot-android/tree/master) called __master__
- A series of Feature branches that look like __CSMC-29_Identify_and_remove_developer_tools__

### General RedPill-Chat development is done by 
- Creating a feature branch from __develop__
  - Use __issue_ number_issue_title__ to name the branch
  - For example __CSMC-29_Identify_and_remove_developer_tools__
- When Code complete, create a Merge request for the __develop__ branch
  - Assign it to a reviewer
- When review is complete the code is merged into __develop __

### Updating RedPill-Chat from Riot-Android
- Pull the latest code from __Riot-Android__ into master
- Code Review Changes
- Merge or Cherry Pick changes into Develop

