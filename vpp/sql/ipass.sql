ALTER TABLE page DROP FOREIGN KEY FKpage695732;
ALTER TABLE pageLid DROP FOREIGN KEY FKpageLid998175;
ALTER TABLE pageLid DROP FOREIGN KEY FKpageLid886583;
ALTER TABLE post DROP FOREIGN KEY FKpost983184;
ALTER TABLE post DROP FOREIGN KEY FKpost871592;
ALTER TABLE vote DROP FOREIGN KEY FKvote377775;
ALTER TABLE vote DROP FOREIGN KEY FKvote804422;
ALTER TABLE user_chat DROP FOREIGN KEY FKuser_chat410197;
ALTER TABLE user_chat DROP FOREIGN KEY FKuser_chat558345;
ALTER TABLE chatMessage DROP FOREIGN KEY FKchatMessag169864;
ALTER TABLE chatMessage DROP FOREIGN KEY FKchatMessag21716;
ALTER TABLE post DROP FOREIGN KEY FKpost75210;
ALTER TABLE media_post DROP FOREIGN KEY FKmedia_post672901;
ALTER TABLE media_post DROP FOREIGN KEY FKmedia_post745297;
ALTER TABLE page DROP FOREIGN KEY FKpage535079;
ALTER TABLE newPassword DROP FOREIGN KEY FKnewPasswor304528;
ALTER TABLE media DROP FOREIGN KEY FKmedia538898;
ALTER TABLE `user` DROP FOREIGN KEY FKuser861103;
ALTER TABLE lidAanvraag DROP FOREIGN KEY FKlidAanvraa697440;
ALTER TABLE lidAanvraag DROP FOREIGN KEY FKlidAanvraa809032;
ALTER TABLE pushReceiver DROP FOREIGN KEY FKpushReceiv26442;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS page;
DROP TABLE IF EXISTS chatMessages;
DROP TABLE IF EXISTS pageLid;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS chat;
DROP TABLE IF EXISTS user_chat;
DROP TABLE IF EXISTS chatMessage;
DROP TABLE IF EXISTS media;
DROP TABLE IF EXISTS media_post;
DROP TABLE IF EXISTS newPassword;
DROP TABLE IF EXISTS lidAanvraag;
DROP TABLE IF EXISTS pushReceiver;
CREATE TABLE `user` (ID varchar(36) NOT NULL, email varchar(100) NOT NULL UNIQUE, hash varchar(88), salt varchar(86), userKey varchar(36) NOT NULL, privatePageId varchar(36), PRIMARY KEY (ID), UNIQUE INDEX (ID));
CREATE TABLE page (ID varchar(36) NOT NULL, name varchar(100) NOT NULL, ownerId varchar(36) NOT NULL, logo varchar(36), PRIMARY KEY (ID), UNIQUE INDEX (ID));
CREATE TABLE chatMessages ();
CREATE TABLE pageLid (userID varchar(36) NOT NULL, pageID varchar(36) NOT NULL, PRIMARY KEY (userID, pageID));
CREATE TABLE post (ID varchar(36) NOT NULL, userID varchar(36) NOT NULL, pageID varchar(36), repliedTo varchar(36), `date` timestamp NOT NULL, text text NOT NULL, PRIMARY KEY (ID), UNIQUE INDEX (ID));
CREATE TABLE vote (postID varchar(36) NOT NULL, userID varchar(36) NOT NULL, vote tinyint, PRIMARY KEY (postID, userID));
CREATE TABLE chat (ID varchar(36) NOT NULL, PRIMARY KEY (ID), UNIQUE INDEX (ID));
CREATE TABLE user_chat (userID varchar(36) NOT NULL, chatID varchar(36) NOT NULL, PRIMARY KEY (userID, chatID));
CREATE TABLE chatMessage (userID varchar(36) NOT NULL, chatID varchar(36) NOT NULL, message text NOT NULL, `date` timestamp NOT NULL);
CREATE TABLE media (ID varchar(36) NOT NULL, location varchar(50) NOT NULL, mime varchar(100) NOT NULL, owner varchar(36) NOT NULL, PRIMARY KEY (ID), UNIQUE INDEX (ID));
CREATE TABLE media_post (mediaid varchar(36) NOT NULL, postID varchar(36) NOT NULL, PRIMARY KEY (mediaid, postID));
CREATE TABLE newPassword (validUntil date NOT NULL, code varchar(36) NOT NULL, userID varchar(36) NOT NULL);
CREATE TABLE lidAanvraag (pageID varchar(36) NOT NULL, userID varchar(36) NOT NULL, PRIMARY KEY (pageID, userID));
CREATE TABLE pushReceiver (auth varchar(32) NOT NULL, endpoint varchar(256) NOT NULL, `key` varchar(128) NOT NULL, userID varchar(36) NOT NULL, PRIMARY KEY (auth), UNIQUE INDEX (auth));
ALTER TABLE page ADD CONSTRAINT FKpage695732 FOREIGN KEY (ownerId) REFERENCES `user` (ID) ON DELETE Cascade;
ALTER TABLE pageLid ADD CONSTRAINT FKpageLid998175 FOREIGN KEY (userID) REFERENCES `user` (ID) ON DELETE Cascade;
ALTER TABLE pageLid ADD CONSTRAINT FKpageLid886583 FOREIGN KEY (pageID) REFERENCES page (ID) ON DELETE Cascade;
ALTER TABLE post ADD CONSTRAINT FKpost983184 FOREIGN KEY (userID) REFERENCES `user` (ID) ON DELETE Cascade;
ALTER TABLE post ADD CONSTRAINT FKpost871592 FOREIGN KEY (pageID) REFERENCES page (ID) ON DELETE Cascade;
ALTER TABLE vote ADD CONSTRAINT FKvote377775 FOREIGN KEY (postID) REFERENCES post (ID) ON DELETE Cascade;
ALTER TABLE vote ADD CONSTRAINT FKvote804422 FOREIGN KEY (userID) REFERENCES `user` (ID) ON DELETE Cascade;
ALTER TABLE user_chat ADD CONSTRAINT FKuser_chat410197 FOREIGN KEY (userID) REFERENCES `user` (ID) ON DELETE Cascade;
ALTER TABLE user_chat ADD CONSTRAINT FKuser_chat558345 FOREIGN KEY (chatID) REFERENCES chat (ID) ON DELETE Cascade;
ALTER TABLE chatMessage ADD CONSTRAINT FKchatMessag169864 FOREIGN KEY (userID) REFERENCES `user` (ID) ON DELETE Cascade;
ALTER TABLE chatMessage ADD CONSTRAINT FKchatMessag21716 FOREIGN KEY (chatID) REFERENCES chat (ID) ON DELETE Cascade;
ALTER TABLE post ADD CONSTRAINT FKpost75210 FOREIGN KEY (repliedTo) REFERENCES post (ID) ON DELETE Cascade;
ALTER TABLE media_post ADD CONSTRAINT FKmedia_post672901 FOREIGN KEY (mediaid) REFERENCES media (ID) ON DELETE Cascade;
ALTER TABLE media_post ADD CONSTRAINT FKmedia_post745297 FOREIGN KEY (postID) REFERENCES post (ID) ON DELETE Cascade;
ALTER TABLE page ADD CONSTRAINT FKpage535079 FOREIGN KEY (logo) REFERENCES media (ID) ON DELETE Cascade;
ALTER TABLE newPassword ADD CONSTRAINT FKnewPasswor304528 FOREIGN KEY (userID) REFERENCES `user` (ID) ON DELETE Cascade;
ALTER TABLE media ADD CONSTRAINT FKmedia538898 FOREIGN KEY (owner) REFERENCES `user` (ID) ON DELETE Cascade;
ALTER TABLE `user` ADD CONSTRAINT FKuser861103 FOREIGN KEY (privatePageId) REFERENCES page (ID) ON DELETE Cascade;
ALTER TABLE lidAanvraag ADD CONSTRAINT FKlidAanvraa697440 FOREIGN KEY (pageID) REFERENCES page (ID) ON DELETE Cascade;
ALTER TABLE lidAanvraag ADD CONSTRAINT FKlidAanvraa809032 FOREIGN KEY (userID) REFERENCES `user` (ID) ON DELETE Cascade;
ALTER TABLE pushReceiver ADD CONSTRAINT FKpushReceiv26442 FOREIGN KEY (userID) REFERENCES `user` (ID) ON DELETE Cascade;
