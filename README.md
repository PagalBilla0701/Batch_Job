To commit and push your current changes to the branch origin/feature/STORY-6503163, follow these steps:


---

Step 1: Verify the Target Branch Exists

Check if the branch feature/STORY-6503163 exists locally or on the remote:

git branch -a

If you see remotes/origin/feature/STORY-6503163 in the list, the branch exists on the remote.


---

Step 2: Switch to the Target Branch

1. If the branch exists locally:

git checkout feature/STORY-6503163


2. If the branch only exists on the remote:

git checkout -b feature/STORY-6503163 origin/feature/STORY-6503163




---

Step 3: Move the Commit to the Target Branch

1. Ensure your changes are committed. If your changes are already committed to main (as seen in your log), you can move them to the target branch by running:

git checkout feature/STORY-6503163
git cherry-pick 11451960ebfef13c178871e8dc6e422d6beb7828


2. Push the changes to the target branch:

git push origin feature/STORY-6503163




---

Step 4: Reset main (Optional)

To remove the commit from main after moving it:

1. Switch to the main branch:

git checkout main


2. Reset main to the previous commit:

git reset --hard HEAD~1


3. Push the updated main branch to the remote:

git push origin main --force

⚠️ Warning: Only force push if you're sure no one else is working on the main branch.




---

After this, your commit will only exist in the feature/STORY-6503163 branch. Let me know if you encounter any issues!

