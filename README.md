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
#### Strategy Overview
We will try and move all code changes in Riot-Android to RedPill-Chat during active development. When we move to release testing we will stop pulling changes from Riot_Android except for critical bugs. If a Riot-Android change looks dangerous or difficult it can be ignored.
#### How to
- Pull the latest code from [Riot-Android/master](https://github.com/vector-im/riot-android/tree/master) into __master__
- Code Review Changes for potential issues
- Merge or Cherry Pick changes into __develop__

