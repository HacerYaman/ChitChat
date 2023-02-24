package com.baitenthy.chitchat.Models;

public class Users {
        private String username;
        private String fullname;
        private String email;
        private String imageurl;
        private String userid;
        private String aboutMe;
        //--------
        private String lastMessage;
        private String password;
        private String status;

        public Users() {
        }


        public Users(String username, String email, String password) {          // for registering. 4. video 18.50
                this.username = username;
                this.email = email;
                this.password = password;
        }

        public Users(String username, String fullname, String email, String imageurl, String userid, String aboutMe, String status) {
                this.username = username;
                this.fullname = fullname;
                this.email = email;
                this.imageurl = imageurl;
                this.userid = userid;
                this.aboutMe = aboutMe;
                this.status=status;
        }

        public String getAboutMe() {
                return aboutMe;
        }

        public void setAboutMe(String aboutMe) {
                this.aboutMe = aboutMe;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getFullname() {
                return fullname;
        }

        public void setFullname(String fullname) {
                this.fullname = fullname;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getImageurl() {
                return imageurl;
        }

        public void setImageurl(String imageurl) {
                this.imageurl = imageurl;
        }

        public String getUserid() {
                return userid;
        }

        public void setUserid(String userid) {
                this.userid = userid;
        }

        public String getLastMessage() {
                return lastMessage;
        }

        public void setLastMessage(String lastMessage) {
                this.lastMessage = lastMessage;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }
}
