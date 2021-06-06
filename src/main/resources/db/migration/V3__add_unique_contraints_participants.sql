ALTER TABLE registration_app.participants ADD CONSTRAINT mobile_uniq UNIQUE (mobile);
ALTER TABLE registration_app.participants ADD CONSTRAINT mail_uniq UNIQUE (mail);
