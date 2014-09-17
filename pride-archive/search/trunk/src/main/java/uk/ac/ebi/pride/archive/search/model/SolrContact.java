package uk.ac.ebi.pride.archive.search.model;

import uk.ac.ebi.pride.archive.dataprovider.person.ContactProvider;
import uk.ac.ebi.pride.archive.dataprovider.person.Title;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class SolrContact implements ContactProvider
{

    private String firstName;

    private String lastName;

    private String affiliation;


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }


    public SolrContact(String json) {

    }

    @Override
    public Title getTitle() {
        return null;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getAffiliation() {
        return affiliation;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public Long getId() {
        return null;
    }
}
