https://docs.google.com/document/d/1pu5TkO5a_d39PlY7oaptGDFexPcglzWv/edit?usp=sharing&ouid=112746384934313058598&rtpof=true&sd=true

# POST example in swagger
# user
{
"name": "Alice",
"email": "alice@example.com",
"role": "MANAGER",
"password": "secure123"
}


# project
{
"name": "Project A",
"createdAt": "2025-06-09T22:00:00",
"owner": {
"id": 1
}
}


# deadline
{
"dueDate": "2025-06-10T18:00:00",
"notified": false
}



# task
{
"title": "Finish Report",
"description": "Prepare final report for project A",
"completed": false,
"assignedTo": {
"id": 1

},
"project": {
"id": 1
}
}
