package laustrup.models;

import laustrup.utilities.collections.lists.Liszt;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

/**
 * Consists of stories which basically is a log or event that has occurred and stamped for time.
 */
@Getter
public class History {

    /**
     * A hex value that defines this specific history.
     */
    private UUID _id;

    /**
     * A kind of collection of event or log that has occurred and stamped for time.
     * Only add stories, don't delete.
     */
    private final Liszt<Story> _stories;

    /**
     * Converts the data transport object into this history.
     * @param dto The data transport object to be converted.
     */
    public History(DTO dto) {
        _id = dto.getId();
        _stories = new Liszt<>(
                Arrays.stream(dto.getStories())
                        .map(Story::new)
                        .isParallel()
        );
    }

    /**
     * Initiates with a collection of stories, meant for getting from database.
     * @param id A hex value that defines this specific history.
     * @param stories Collection of event or log that has occurred and stamped for time.
     */
    public History(UUID id, Liszt<Story> stories) {
        _id = id;
        _stories = stories;
    }

    /**
     * Initiates with an empty Liszt of stories.
     */
    public History() {
        _stories = new Liszt<>();
    }

    /**
     * Adds a story.
     * @param story The story to be added.
     * @return The total collection of stories.
     */
    public Liszt<Story> add(Story story) {
        return _stories.Add(story);
    }

    /**
     * A kind of event or log that has occurred and stamped for time.
     */
    @Getter
    public static class Story {

        /**
         * A hex value that defines this specific story.
         */
        private UUID _id;

        /**
         * A short title for the story and the description.
         */
        private final String _title;

        /**
         * Describes to the User about what happened in the story.
         */
        private final String _description;

        /**
         * Some options to put the story in category.
         */
        private final Type _type;

        /**
         * The time that the story occurred.
         */
        private final LocalDateTime _timestamp;

        /**
         * Converts the data transport object into a story.
         * @param dto The data transport object to be converted.
         */
        public Story(DTO dto) {
            _id = dto.getId();
            _title = dto.getTitle();
            _description = dto.getDescription();
            _type = dto.getType();
            _timestamp = dto.getTimestamp();
        }

        /**
         * With all values, meant for being from database.
         * @param id A hex value that defines this specific history.
         * @param title A short title for the story and the description.
         * @param description Describes to the User about what happened in the story.
         * @param type Some options to put the story in category.
         * @param timestamp The time that the story occurred.
         */
        public Story(UUID id, String title, String description, Type type, LocalDateTime timestamp) {
            _id = id;
            _title = title;
            _description = description;
            _type = type;
            _timestamp = timestamp;
        }

        /**
         * Meant for creating new story, will autogenerate timestamp as now.
         * @param title A short title for the story and the description.
         * @param description Describes to the User about what happened in the story.
         * @param type Some options to put the story in category.
         */
        public Story(String title, String description, Type type) {
            _title = title;
            _description = description;
            _type = type;
            _timestamp = LocalDateTime.now();
        }

        /**
         * A kind of category of the event that occurred in the story.
         */
        public enum Type {
            CAUTIOUS,
            UNIMPORTANT,
            APPROVE,
            DECLINE,
            CREATE,
            READ,
            UPDATE,
            DELETE
        }

        /**
         * The data transport object of story.
         */
        @Getter @Setter
        public static class DTO {

            /**
             * A hex value that defines this specific story.
             */
            private UUID id;

            /**
             * A short title for the story and the description.
             */
            private String title;

            /**
             * Describes to the User about what happened in the story.
             */
            private String description;

            /**
             * Some options to put the story in category.
             */
            private Type type;

            /**
             * The time that the story occurred.
             */
            private LocalDateTime timestamp;

            /**
             * Converts the story into the data transport object.
             * @param story The story to be converted into this data transport object.
             */
            public DTO(Story story) {
                id = story._id;
                title = story.get_title();
                description = story.get_description();
                type = story.get_type();
                timestamp = story.get_timestamp();
            }
        }
    }

    /**
     * The data transport object of history.
     */
    @Getter
    public static class DTO {

        /**
         * A hex value that defines this specific history.
         */
        private UUID id;

        /**
         * A kind of collection of event or log that has occurred and stamped for time.
         */
        private Story.DTO[] stories;

        /**
         * Converts the history into this data transport object.
         * @param history The history to be converted into this data transport object
         */
        public DTO(History history) {
            id = history._id;
            stories = history.get_stories().stream()
                    .map(Story.DTO::new)
                    .toArray(Story.DTO[]::new)
            ;
        }
    }
}
