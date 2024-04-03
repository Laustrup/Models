package laustrup.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The base of many objects, that share these same attributes.
 * When it is created through a constructor, that doesn't ask for a DateTime.
 * It will use the DateTime of now.
 */
public abstract class Model {

    /**
     * The identification value in the database for a specific entity.
     * Must be unique, if there ain't other ids for this entity.
     * UUIDs are unique hex decimal values of the specific entity.
     */
    @Getter
    protected UUID _primaryId;

    /**
     * Another identification value in the database for a specific entity.
     * Must be unique with primary id.
     * Is used for incidents, where there are a connection between two entities,
     * and they are both being used as primary keys
     */
    protected UUID _secondaryId;

    /**
     * Gets the secondary id, if there isn't any, it will get the primary id.
     * @return The gathered id.
     */
    public UUID get_secondaryId() {
        return _secondaryId == null ? get_primaryId() : _secondaryId;
    }

    /**
     * The name for an entity or model.
     * Can be of different purposes,
     * such as username or simply for naming a unit.
     */
    @Getter @Setter
    protected String _title;

    /** Specifies the time this entity was created. */
    @Getter
    protected LocalDateTime _timestamp;

    /**
     * Simply said, this is used to describe a relevant situation.
     * Useful to identify an incident or change.
     * Is added to the Response entity class when answering.
     */
    @Getter
    protected Situation _situation = Situation.NONE;

    /**
     * Sets the Situation.
     * The situation mustn't become none after it has had a situation,
     * then it must be resolved, which this method insures to prevent misinformation.
     * @param situation The new Situation.
     * @return The current Situation.
     */
    public Situation set_situation(Situation situation) {
        _situation = situation == Situation.NONE && _situation != Situation.NONE
            ? Situation.RESOLVED
            : situation;

        return _situation;
    }

    /** For the defineToString of how it should be split. */
    private final String _toStringFieldSplitter = ",\n \t",
            _toStringKeyValueSplitter = ":\t";

    public Model(ModelDTO model) {
        _primaryId = model.getPrimaryId();
        _secondaryId = model.getSecondaryId();
        _title = model.getTitle();
        _timestamp = model.getTimestamp();
        _situation = model.getSituation();
    }

    /** Will generate a timestamp of the moment now in datetime. */
    public Model() {
        _timestamp = LocalDateTime.now();
    }

    /**
     * Will generate a timestamp of the moment now in datetime.
     * @param title A title describing this entity internally.
     */
    public Model(String title) {
        _title = title;
        _timestamp = LocalDateTime.now();
    }

    /**
     * @param title A title describing this entity internally.
     * @param timestamp Specifies the time this entity was created.
     */
    public Model(String title, LocalDateTime timestamp) {
        _title = title;
        _timestamp = timestamp;
    }

    /**
     * @param id A hex decimal value identifying this item uniquely.
     * @param title A title describing this entity internally.
     * @param timestamp Specifies the time this entity was created.
     */
    public Model(UUID id, String title, LocalDateTime timestamp) {
        _primaryId = id;
        _title = title;
        _timestamp = timestamp;
    }

    /**
     * Will generate a timestamp of the moment now in datetime.
     * @param primaryId A hex decimal value identifying this item uniquely.
     * @param secondaryId Another hex decimal value identifying another item uniquely.
     * @param title A title describing this entity internally.
     */
    public Model(UUID primaryId, UUID secondaryId, String title) {
        _primaryId = primaryId;
        _secondaryId = secondaryId;
        _title = title;
        _timestamp = LocalDateTime.now();
    }

    /**
     * @param primaryId A hex decimal value identifying this item uniquely.
     * @param secondaryId Another hex decimal value identifying another item uniquely.
     * @param title A title describing this entity internally.
     * @param timestamp Specifies the time this entity was created.
     */
    public Model(UUID primaryId, UUID secondaryId, String title, LocalDateTime timestamp) {
        _primaryId = primaryId;
        _secondaryId = secondaryId;
        _title = title;
        _timestamp = timestamp;
    }


    /**
     * Checks if secondary id is null.
     * @return True if secondary id isn't null.
     */
    public boolean hasSecondaryId() { return _secondaryId != null; }

    /**
     * Will generate a toString from the attributes and values.
     * Makes it able to have the same structure for all objects.
     * If there is more value inputs than keys, the toString will not be unique, even though it must.
     * @param title The class name of the class Model, always use getClass().getSimpleName().
     * @param values First array is a String array of keys and the other are its values.
     * @return The generated toString.
     */
    protected String defineToString(String title, String[][] values) {
        return defineToString(title, values[0], values[1]);
    }

    /**
     * Will generate a toString from the attributes and values.
     * Makes it able to have the same structure for all objects.
     * If there is more value inputs than keys, the toString will not be unique, even though it must.
     * @param title The class name of the class Model, always use getClass().getSimpleName().
     * @param keys The attributes of the class Model, visualized as keys.
     * @param values The values of the attributes/keys, as Strings.
     * @return The generated toString.
     */
    protected String defineToString(String title, String[] keys, String[] values) {
        StringBuilder content = new StringBuilder();

        if (values.length <= keys.length)
            for (int i = 0; i < keys.length; i++) {
                content.append(keys[i]).append(_toStringKeyValueSplitter).append(values[i]);
                if (i < keys.length-1)
                    content.append(_toStringFieldSplitter);
            }
        else
            content = new StringBuilder("Content couldn't be generated, since there are less attributes than values");

        return title + "(\n \t" + content + "\n)";
    }

    /**
     * The base of many objects, that share these same attributes.
     * When it is created through a constructor, that doesn't ask for a DateTime.
     * It will use the DateTime of now.
     */
    @Getter @Setter
    public static class ModelDTO {

        /**
         * The identification value in the database for a specific entity.
         * Must be unique, if there ain't other ids for this entity.
         * UUIDs are unique hex decimal values of the specific entity.
         */
        protected UUID primaryId;

        /**
         * Another identification value in the database for a specific entity.
         * Must be unique with primary id.
         * Is used for incidents, where there are an connection between two entities,
         * and they are both being used as primary keys
         */
        protected UUID secondaryId;

        /**
         * The name for an entity or model.
         * Can be of different purposes,
         * such as username or simply for naming a unit.
         */
        protected String title;

        /** Specifies the time this entity was created. */
        protected LocalDateTime timestamp;

        /**
         * Simply said, this is used to describe a relevant situation.
         * Useful to identify an incident or change.
         * Is added to the Response entity class when answering.
         */
        protected Situation situation;

        public ModelDTO(Model model) {
            primaryId = model.get_primaryId();
            secondaryId = model.get_secondaryId();
            title = model.get_title();
            timestamp = model.get_timestamp();
            situation = model.get_situation();
        }
    }
}
