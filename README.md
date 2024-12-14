Here’s a corrected version of your email:


---

Subject: Concern Regarding Code Changes and JUnit Coverage

Hi @B, Sindhanavas/@Balasubramaniam, Loganathan,

I wanted to raise a concern regarding the Phase 1 code. The main classes that include most of the changes for Drop 1, and even the ongoing changes for Drop 2, are CallActivityController and CallActivityServiceImpl. These are the classes where most of the changes are being made, often without my knowledge. Every time new code is added, it reduces the code coverage of my previous work. For instance, last Friday, Farzeen committed some code to both of these classes (CallActivityController and CallActivityServiceImpl), and I was not aware of the development activity being done. This meant it took me additional time to analyze the changes before I could write the corresponding JUnit tests.

Meanwhile, if someone else also commits code to these same classes, the code coverage will be further reduced, and I will have to do the rework—analyzing the code and then writing the JUnit tests for the new code. As a result, this will prevent us from meeting the estimated deadline.

Therefore, I request that anyone making changes to the code should write the JUnit tests for their own changes. I can write JUnit tests for methods that will not require further changes in the near future.

I have already written JUnit tests for the newly introduced SRAPI and CTOM API, and they are successfully providing the required code coverage.

Best regards,
Kaushik Singh


---

This version should now be grammatically correct and clear. Let me know if you'd like to adjust anything further!

