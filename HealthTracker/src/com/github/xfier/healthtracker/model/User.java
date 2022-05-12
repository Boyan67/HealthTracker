package com.github.xfier.healthtracker.model;

import com.github.xfier.healthtracker.model.data.Email;
import com.github.xfier.healthtracker.model.data.Height;
import com.github.xfier.healthtracker.model.data.Weight;
import com.github.xfier.healthtracker.model.group.Group;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

// TODO: ensure validation in setters align with database restrictions.
/**
 * Mutable model for a user of the System.
 * <p>
 * Initialised with {@link Builder} to ensure required fields are set.
 * Note that some setters explicitly check for null, while others do not need
 * to as a NullPointerException will be thrown during validation in that case.
 * @author Adam Fish
 */
public class User
{
    /**
     * Minimum lengths for the username, forename & surname fields.
     */
    private static final int NAME_LEN_MIN = 2;

    /**
     * Maximum lengths for the username, forename & surname fields.
     */
    private static final int NAME_LEN_MAX = 40;

    /**
     * Error message to display if name is out of bounds.
     */
    private static final String NAME_ERR = " must be between " + NAME_LEN_MIN + " and " + NAME_LEN_MAX + " characters";

    /**
     * Error message to display if user is missing required field.
     */
    private static final String STATE_ERR = "Missing required field: ";

    // attributes - see setters for restrictions
    // -----------------------------------------
    private String username;
    private String forename;
    private String surname;
    private Email email;
    private LocalDate dateOfBirth;  // much better than java.util.Date - immutable for one
    private Height height;
    private Weight weight;
    private final Set<Goal> unmetGoals;
    private final Set<Goal> metGoals;
    private final Set<Group> groups;

    // cannot be directly instantiated
    // -------------------------------
    private User()
    {
        unmetGoals = new HashSet<>();
        metGoals   = new HashSet<>();
        groups     = new HashSet<>();
    }

    /**
     * Class following the Builder design pattern for User construction, due to
     * the large number of fields requiring initialisation.
     * <p>
     * User is mutable, but the fields must be initialised (setters ensure
     * mutation maintains validity), hence the need for a builder.
     */
    public static class Builder
    {
        private final User user;

        /**
         * Initialise new user.
         * @param username username (can be changed later)
         */
        public Builder(String username)
        {
            user = new User();
            user.setUsername(username);
        }

        // required fields
        // ---------------
        public Builder withForename(String forename)  { user.setForename(forename); return this; }
        public Builder withSurname(String surname)    { user.setSurname(surname);   return this; }
        public Builder withEmail(Email email)         { user.setEmail(email);       return this; }
        public Builder withDateOfBirth(LocalDate dob) { user.setDateOfBirth(dob);   return this; }
        public Builder withHeight(Height height)      { user.setHeight(height);     return this; }
        public Builder withWeight(Weight weight)      { user.setWeight(weight);     return this; }

        /**
         * Set an optional initial weight goal for the user.
         * @param goal weight goal
         * @return this Builder
         */
        public Builder withTargetWeight(WeightGoal goal) { user.addGoal(goal); return this; }

        /**
         * Construct a new valid User, ensuring all required fields are initialised.
         * @return new user
         */
        public User build()
        {
            if (user.forename    == null) throw new IllegalStateException(STATE_ERR + "forename");
            if (user.surname     == null) throw new IllegalStateException(STATE_ERR + "surname");
            if (user.email       == null) throw new IllegalStateException(STATE_ERR + "email");
            if (user.dateOfBirth == null) throw new IllegalStateException(STATE_ERR + "dateOfBirth");
            if (user.height      == null) throw new IllegalStateException(STATE_ERR + "height");
            if (user.weight      == null) throw new IllegalStateException(STATE_ERR + "weight");
            return user;
        }
    }

    @Override
    public String toString() { return getUsername() + " [" + getEmail() + "]"; }

    /**
     * Set username, ensuring it is between {@link #NAME_LEN_MIN} and
     * {@link #NAME_LEN_MAX}.
     * @param username new username
     */
    public void setUsername(String username)
    {
        if (username.length() < NAME_LEN_MIN || username.length() > NAME_LEN_MAX)
            throw new IllegalArgumentException("username" + NAME_ERR);
        this.username = username;
    }
    public String getUsername() { return username; }

    /**
     * Set forename, ensuring it is between {@link #NAME_LEN_MIN} and
     * {@link #NAME_LEN_MAX}.
     * @param forename new forename
     */
    public void setForename(String forename)
    {
        if (forename.length() < NAME_LEN_MIN || forename.length() > NAME_LEN_MAX)
            throw new IllegalArgumentException("forename" + NAME_ERR);
        this.forename = forename;
    }
    public String getForename() { return forename; }

    /**
     * Set surname, ensuring it is between {@link #NAME_LEN_MIN} and
     * {@link #NAME_LEN_MAX}.
     * @param surname new surname
     */
    public void setSurname(String surname)
    {
        if (surname.length() < NAME_LEN_MIN || surname.length() > NAME_LEN_MAX)
            throw new IllegalArgumentException("surname" + NAME_ERR);
        this.surname = surname;
    }
    public String getSurname() { return surname; }

    /**
     * Set email, ensuring it is non-null.
     * @param email new email
     */
    public void setEmail(Email email)
    {
        if (email == null) throw new NullPointerException();
        this.email = email;

    }
    public Email getEmail() { return email; }

    /**
     * Set date of birth, ensuring it is not in the future.
     * @param dob new date of birth
     */
    public void setDateOfBirth(LocalDate dob)
    {
        if (dob.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        dateOfBirth = dob;
    }
    public LocalDate getDateOfBirth() { return dateOfBirth; }

    /**
     * Set height ensuring it is non-null.
     * @param height new height
     */
    public void setHeight(Height height)
    {
        if (height == null) throw new NullPointerException();
        this.height = height;
    }
    public Height getHeight() { return height; }

    /**
     * Set weight ensuring it is non-null.
     * @param weight new weight
     */
    public void setWeight(Weight weight)
    {
        if (weight == null) throw new NullPointerException();
        this.weight = weight;
    }
    public Weight getWeight() { return weight; }

    /**
     * Get BMI using metric values.
     * @return user BMI
     */
    public double getBMI()
    {
        double metresTall = ((double)getHeight().getCm())/100;
        return getWeight().getKg()/(metresTall*metresTall);
    }

    /**
     * Get all unmet user goals.
     * @return immutable view of unmet goals
     */
    public Set<Goal> unmetGoals() { return Collections.unmodifiableSet(unmetGoals); }

    /**
     * Get all met goals.
     * @return immutable view of met goals.
     */
    public Set<Goal> metGoals() { return Collections.unmodifiableSet(metGoals); }

    /**
     * Get all unmet goals which have expired.
     * @return expired goals
     */
    public Set<Goal> expiredGoals()
    {
        return unmetGoals.stream().filter(Goal::isExpired).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Mark goal as complete.
     * @param goal completed goal
     * @return whether goal was successfully completed
     */
    public boolean completeGoal(Goal goal)
    {
        if (unmetGoals.remove(goal)) { return metGoals.add(goal); }
        else return false;
    }

    public boolean addGoal(Goal goal)    { return unmetGoals.add(goal); }
    public boolean removeGoal(Goal goal) { return unmetGoals.remove(goal) | metGoals.remove(goal); }

    /**
     * Get all user groups.
     * @return immutable view of user groups (modify with {@link #addGoal} and
     * {@link #removeGoal})
     */
    public Set<Group> getGroups() { return Collections.unmodifiableSet(groups); }
    public boolean addGroup(Group group)    { return groups.add(group);    }
    public boolean removeGroup(Group group) { return groups.remove(group); }

    /**
     * Test harness.
     * @param args unused
     */
    public static void main(String[] args)
    {
        // builder
        WeightGoal wg = new WeightGoal(LocalDate.now(), "weight goal", new Weight(10, 0));
        User user = new Builder("user123")
                .withForename("John")
                .withEmail(new Email("j.doe@example.com"))
                .withHeight(new Height(5, 10))
                .withTargetWeight(wg)
                .withSurname("Doe")
                .withDateOfBirth(LocalDate.EPOCH)
                .withWeight(new Weight(10, 7))
                .build();

        // toString & getters
        System.out.println(user);
        System.out.println(user.getForename() + " - " + user.getSurname() + " - " +
                user.getDateOfBirth() + " - " + user.getHeight() + " - " + user.getWeight());

        //TODO Uncomment this, currently commented out because of the group additions.
        // goals & groups
        /*Group g = new Group();
        System.out.println(user.addGroup(g));
        System.out.println(user.removeGroup(g));
        user.removeGoal(new WeightGoal(LocalDate.now(), "unused", new Weight(60)));
        System.out.println(user.getGroups());
        System.out.println(user.unmetGoals() + " - " + user.metGoals());
        System.out.println(user.completeGoal(wg));
        System.out.println(user.unmetGoals() + " - " + user.metGoals());
        System.out.println(user.expiredGoals());*/
    }
}
