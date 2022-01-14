package ru.javaops.voting.error;

import org.springframework.http.HttpStatus;

public class UpdateRestrictionException extends AppException {
    public enum RestrictionType {
        VOTE_TIME_RESTRICTION("Forbidden to change your vote after 11:00, it's too late"),
        VOTE_DATE_RESTRICTION("Forbidden to vote not for today's menu"),
        MENU_DATE_RESTRICTION("Forbidden to create or update menus for previous days");

        private final String message;

        RestrictionType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public UpdateRestrictionException(RestrictionType restrictionType) {
        super(HttpStatus.FORBIDDEN, restrictionType.message);
    }
}
