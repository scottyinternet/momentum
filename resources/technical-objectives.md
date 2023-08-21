## Technical Learning Objectives

The following learning objectives must be included in your project. You should review this document as you are designing the project to ensure that your project includes all of the objectives. When we wrap up the project in a few weeks you'll come back to this document and fill it out as part of your reflection/retrospective process.

### API Design

**Design an API to meet the needs of your application.** Provide a link to the definition for your endpoints (can be code/configuration, or can be documentation). List one thing that your team learned about designing a good API.

*Endpoint definition location:*
*What we learned:*

**Develop a service endpoint definition that uses complex inputs and outputs.** Select one of your endpoints and list the operation’s input and output objects. Under each, list its attributes.

*Endpoint:*
*Input object(s):*

* attribute 1
* ...

*Output object(s):*

* attribute 1
* ...

**Given a user story that requires a user to provide values to a service endpoint, design a service endpoint including inputs, outputs, and errors.** Select one of your endpoints that accepts input values from a client. List the error cases you identified and how the service responds in each case. Provide at most 3 error cases.

|**Endpoint:**  |                     |
|---            |---                  |
|**Error case** |**Service response** |
|               |                     |
|               |                     |
|               |                     |

**Develop a service endpoint definition that uses query parameters to determine how results are sorted or filtered.** List at least one endpoint that allows sorting or filtering of results. Which attribute(s) can be sorted/filtered on?

*Endpoint:*
*Attribute(s):*

**Determine whether a given error condition should result in a client or server exception.** List one client exception and one server exception that your service code throws. List one condition in which this exception is thrown.

|                       |**Exception** |**One case in which it is thrown** |
|---	                |---	       |---	                               |
|**Client exception:**  |	           |	                               |
|**Service exception:** |	           |	                               |

### DynamoDB Table Design

**Decompose a given set of use cases into a set of DynamoDB tables that provides efficient data access.** List the DynamoDB tables in your project:

1.
2.
3.


**Design a DynamoDB table key schema that allows items to be uniquely identified.** Describe the primary key structure for your DynamoDB table with the most interesting primary key. In a sentence or two, explain your choice of partition/sort key(s).

-

**Design the attributes of a DynamoDB table given a set of use cases.** List a DynamoDB table with at least 3 attributes. List one relevant use case that uses the attribute. In one sentence, describe why the attribute is included.

**Table name:**

**Attributes:**

|Attribute name |(One) relevant use case |attribute purpose |
|---            |---                     |---               |
|               |                        |                  |
|               |                        |                  |
|               |                        |                  |
|               |                        |                  |
|               |                        |                  |

### DynamoDB Indexes

**Design a GSI key schema and attribute projection that optimizes queries not supported by a provided DynamoDB table.** In one or two sentences, explain why you created one of the GSIs that your project uses, including one use case that uses that index.

**Table name:**

**Table keys:**

**GSI keys:**

**Use case for GSI:**

**Implement functionality that uses query() to retrieve items from a provided DynamoDB's GSI.** List one of your use cases that uses `query()` on a GSI.

**Table name:**

**Use case for `query()` on GDI:**

## Soft(er) Outcomes

**Learn a new technology.** For each team member, list something new that that team member learned:

| Name |Something new the team member learned |
|------|---                                   |
|      |                                      |
|      |                                      |
|      |                                      |
|      |                                      |

**Identify key words to research to accomplish a technical goal. Use sources like Stack Overflow to solve issues encountered while programming.** Give an example of a search term that your team might have used to find an answer to a technical question/obstacle that your team ran into. List the resource that you found that was helpful.

**Search terms:**
**Helpful resource:**

**Find material online to learn new technical topics.** List one resource that your team found on your own that helped you on your project.

**Topic:**

**Resource:**

**Participate in a design review to receive feedback on your design.** List one change or clarification that your team made after holding a review of your design (either from a peer or instructor). (If there are comments/questions still in the design doc, that might save you some time remembering...)

1.

**Participate in a design review to give feedback on another group's design.** List one question or piece of feedback that you remember giving the other team at the time of their review that was helpful or insightful. (If there are comments/questions still in the design doc, that might save you some time remembering...)

1.

**Share what was worked on yesterday, the plan for today, and any blockers as a part of a scrum standup.** In one or two sentences, describe what your team found to be the most useful outcome from holding daily standups.

1.

