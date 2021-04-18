# realtime-search-demo
A basic project which demonstrates ease of using Lucene for Geo-spatial searches, with a simple UI

### Problem Statement:
Fetching X Scooters within a radius (Y).

Your goal is to code a system that can fetch the closest x scooters for a given lat,lon within y meters and plot it on a map.

Steps:
a.) Create the data storage for the scooters and populate data for scooters placed randomly throughout Singapore.
b.) Write a backend service that can provide data to the front end for plotting the closest x scooters within y meters of a given lat,lon
c.) Write front end that allows you to set:
x scooters
y search radius
lat, and long
It should fetch the data based on these params from the backend service, and it should plot on a map.


### Running the project

#### Backend (demo)

Backend is using Spring Boot, with dependencies for Lucene (https://lucene.apache.org/), which provides storage and search capabilities embeddable to the project. In the backend application, it uses RAM as data storage, so once the app is killed, the stored data would be lost. 
There are different options for persisting data and using a RDBMS. Technically, from the application perspective there is no difference between using any sort of persisting options, and in order to save time, I have picked RAM Directory. 
The data format on Lucene is internal to itself, hence unlike a RDBMS, it is not human-readable. It is actually optimized for separating search and data storage, hence reducing the load on the storage part, however, it is suitable for storing search critical data, lat and long values for the task’s case. Lucene is the core component of Apache Solr and Elasticsearch projects, so it is quite tested and trustworthy in terms of performance. 

Once the backend application is up, you can generate 200.000 random coords within Singapore by sending an HTTP GET to 

http://localhost:8080/generate

Or alternatively, on the first search request, the application creates random points. (So this would cause a short delay on the frontend application, but only for the very first request.)

#### Frontend (ui)

Frontend application is a ReactJS application, with a simple UI. It has  a map centered in the middle of Singapore, and self-explanatory input fields for searching for the scooters. 

The map API is provided by https://www.here.com/ and you would need to use it in the start up command of the frontend application, or set it as your environment variable.

So the startup command for the frontend application would be 
REACT_APP_HERE_API_KEY=<key_you_get_from www.here.com> npm start

While searching for scooters, you should enter a lat/lon value within Singapore. So you should enter 
lat values between 1.1304753 and 1.4504753 
lon values between 103.6920359 and 104.0120359
On the frontend page, the expected format for lat and lon values are lat,lon without any whitespaces or characters.
