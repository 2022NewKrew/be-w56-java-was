from locust import HttpUser,TaskSet,task,between

class MyLocus(HttpUser):
   wait_time = between(3,5)

   @task(1)

   def index(self):
       self.client.get("/")
