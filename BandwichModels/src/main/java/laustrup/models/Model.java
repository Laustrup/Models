package laustrup.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The base of many objects, that share these same attributes.
 * When it is created through a constructor, that doesn't ask for a DateTime.
 * It will use the DateTime of now.
 */
public abstract class Model {

    /**
     * The identification value in the database for a specific entity.
     * Must be unique, if there ain't other ids for this entity.
     */
    @Getter
    protected long _primaryId;

    /**
     * Another identification value in the database for a specific entity.
     * Must be unique with primary id.
     * Is used for incidents, where there are a connection between two entities
     * and they are both being used as primary keys
     */
    protected Long _secondaryId;

    /**
     * Gets the secondary id, if there isn't any, it will get the primary id.
     * @return The gathered id.
     */
    public Long get_secondaryId() {
        return _secondaryId == null || _secondaryId < 1 ? get_primaryId() : _secondaryId;
    }

    /**
     * The name for an enitity or model.
     * Can be of different purposes,
     * such as username or simply for naming a unit.
     */
    @Getter @Setter
    protected String _title;

    /** Specifies the time this entity was created. */
    @Getter
    protected LocalDateTime _timestamp;

    /**
     * A status that determines if the Model is being assembled or not.
     * Some methods require this boolean to be true.
     */
    @Getter
    protected boolean _assembling;

    /** A kind of String message, that can be used to define an incident a message. */
    @Getter @Setter
    protected String _situation = "UNDEFINED";

    /** I used for the defineToString of how it should be split. */
    private final String _toStringFieldSplitter = ",\n \t",
            _toStringKeyValueSplitter = ":\t";

    /** Doesn't take any parameters, but will set the timestamp to the present time now. */
    public Model() { _timestamp = LocalDateTime.now(); }

    /**
     * Sets the timestamp to present time now.
     * @param id An unique id value to identify the Model.
     */
    public Model(long id) {
        _primaryId = id;
        _timestamp = LocalDateTime.now();
    }

    /**
     * For newly created Models with a generated id.
     * Sets the timestamp to present time now.
     * @param title The title of the Model, either a given title or created from values.
     */
    public Model(String title) {
        _title = title;
        _timestamp = LocalDateTime.now();
    }

    /**
     * For newly created Models with a generated id.
     * @param title The title of the Model, either a given title or created from values.
     * @param timestamp The time this Model was created.
     */
    public Model(String title, LocalDateTime timestamp) {
        _title = title;
        _timestamp = timestamp;
    }

    /**
     * Only sets the values in the parameter.
     * @param id An unique id value to identify the Model.
     * @param title The title of the Model, either a given title or created from values.
     * @param timestamp The time this Model was created.
     */
    public Model(long id, String title, LocalDateTime timestamp) {
        _primaryId = id;
        _title = title;
        _timestamp = timestamp;
    }

    /**
     * Constructor for a Model with two ids, which could be a conjoint relation Model.
     * @param primaryId The first id.
     * @param secondaryId The second id.
     * @param title The title of the Model, either a given title or created from values.
     */
    public Model(long primaryId, long secondaryId, String title) {
        _primaryId = primaryId;
        _secondaryId = secondaryId;
        _title = title;
        _timestamp = LocalDateTime.now();
    }

    /**
     * Constructor for a Model with two ids, which could be a conjoint relation Model.
     * @param primaryId The first id.
     * @param secondaryId The second id.
     * @param title The title of the Model, either a given title or created from values.
     * @param timestamp The time this Model was created.
     */
    public Model(long primaryId, long secondaryId, String title, LocalDateTime timestamp) {
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
     * Will determine that the status of is being assembling is over by making assembling false.
     * @return The assembling status.
     */
    public boolean doneAssembling() {
        _assembling = false;
        return _assembling;
    }

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
}
