BuddyHang App Idea 
===

# BuddyHang

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
The ultimate hangout planning app where people can plan meetups with each other. From small dinner events at home to a picnic in Central Park, people can collaborate to create a great experience together. 

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Social Networking
- **Mobile:** Mobile first experience
- **Story:** Allows people to organize hangouts with others
- **Market:** Anyone with friends and/or partners can enjoy this app
- **Habit:** Users can use this anytime they want to hangout with people
- **Scope:** Can be expanded to include a story where you can instant message people

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users can login or register for an account and see their profile and preference settings
* Hosts can post an event to their friends/publically and attendees can add themselves to the event so others can know that they are going 
* Profile pages for each user (with information about their social media account usernames, location, etc.) 
* People can take surveys for hangouts (where they can pick the location, food, etc.)

**Optional Nice-to-have Stories**

* Connect to zoom/online video chat to make virtual hangouts
* Play group games 
* QR code with socials 
* Payments for things 
* Hosts can upload receipts and request payments from people/post that people need to venmo them

### 2. Screen Archetypes
* Login/register 
    * User signs up or logs into their account
    * If registering for a new account, the user must fill in information containing their personal information and social media
* Events feed tab 
    * Shows their feed with events that their friends are hosting and user gets the option to attend the event 
    * Users can create a post where they can plan an event
    * Event screen 
        * User can see who else is attending 
    * Post event 
        * Users choose descripters for their event and they can post to their friend group or publically
    * Accepted event 
        * User can take surveys that the host put out
* Notificatons 
    *  Invites to events
    *  Changes to events
* My events (Calendar) 
    * Users can view the events they are attending
* Profile Screen
    * Contains user's personal information (social media) and their friends 
* Settings 
    * Lets people change language, and app notification settings.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Notifications 
* Calendar
* Events feed
* Account
* Settings

**Flow Navigation** (Screen to Screen)

*  Forced Log-in -> Account creation if no log in is available
*  Events feed -> Event planning  -> Attend event screen -> Accepted event screen 
*  Notifications 
*  Calendar
*  Profile
*  Settings

## Wireframes

![](https://i.imgur.com/dAL76f9.jpg|width=20)

### [BONUS] Digital Wireframes & Mockups
* TBD

### [BONUS] Interactive Prototype
* TBD

## Schema 

### Models
User

   | Property          | Type   | Description |
   | ----------------- | ------ | ------------|
   | userId            | String | unique id for the user (default field) |
   | password          | String | password for the user |
   | friends           | Arrays | friends of the user |
   | number of friends | Number | number of friends user has |
   | events            | Arrays | events the user is hosting |
   | events            | Arrays | events the user is attending |

Hangout Event

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user event (default field) |
   | host          | Pointer to User| profile of the host |
   | description   | String   | description of the event |
   | invitees      | Arrays   | people who are invited to the event|
   | attendees     | Arrays   | users who are attending |
   | createdAt     | DateTime | date when event is created (default field) |
   | updatedAt     | DateTime | date when event is last updated (default field) |

### Networking
#### List of network requests by screen

 - Events (home) feed
      - (Read/GET) Fetching/Query all events where user is an invitee
         ```java
           ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
           query.include(Event.KEY_USER);
           query.whereEqualTo(Event.KEY_USER, ParseUser.getCurrentUser());
           query.addDescendingOrder(Event.KEY_CREATED_KEY);
         ```
      - (Create/POST) Create a new event
      - (Delete) Delete existing post 
      - (Delete) Delete events user declines
  - Notifications
      - (Read/GET) Query all posts where user is an invitee
      - (Read/GET) Query all posts where user is an attendee
      - (Read/GET) Query all friend invites 
  - Calendar
      - (Read/GET) Query all posts where user is an attendee
      - (Read/GET) Query all posts where user is an host
  - Account
      - (Read/GET) Query logged in user object 
      - (Update/PUT) Update user profile image/other information 
      - (Update) Update user friends list
      - (Delete) Delete friends of user 
  - Settings
      - (Read/GET) Setting preferences of the user 
      - (Update) Setting preferences of the user
