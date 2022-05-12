package com.github.xfier.healthtracker.model.data;

import java.util.regex.Pattern;

/**
 * Email class containing validation.
 *
 * @author Adam Fish
 */
public class Email
{
    /**
     * Regex pattern to match (hopefully) valid email.
     * <p>
     * More specifically, it matches any email up to 254 total characters of
     * the form 'USER@DOMAIN'.
     * <br>
     * USER can be any alphanumeric and [_.-] chars, up to 64 chars, where the
     * final character is alphanumeric.
     * <br>
     * DOMAIN can be any alphanumeric or hyphen chars followed by a TLD of at
     * least 2 chars.
     */
    private static final Pattern PATTERN =
            Pattern.compile("^(?=\\S{0,254}$)[\\w.-]{0,63}[a-z0-9]@[a-z0-9-]+\\.[a-z0-9-]{2,}$",
            Pattern.CASE_INSENSITIVE);

    /**
     * Internal email representation.
     */
    private final String email;

    /**
     * Create new email.
     *
     * @param email email as string
     * @throws IllegalArgumentException if the email is malformed
     */
    public Email(String email)
    {
        if (!PATTERN.matcher(email).matches()) { throw new IllegalArgumentException("Invalid email: " + email); }
        this.email = email;
    }

    @Override
    public String toString() { return email; }

    /**
     * Test harness.
     * @param args unused
     */
    public static void main(String[] args)
    {
        // test valid email
        Email e1 = new Email("abc-xyz@example.com");
        System.out.println(e1);
    }
}
