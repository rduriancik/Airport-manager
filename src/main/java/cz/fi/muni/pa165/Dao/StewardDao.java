package cz.fi.muni.pa165.Dao;


import cz.fi.muni.pa165.Steward;

import java.util.List;

/**
 * Interface representing basic operations with entity.
 *
 * @author Ondřej Přikryl
 */
public interface StewardDao {

    /**
     * Method which adds steward to database.
     *
     * @param steward to be added.
     */
    public void createSteward(Steward steward);

    /**
     * Method which removes steward from the database.
     *
     * @param steward to be removed.
     */
    public void deleteSteward(Steward steward);

    /**
     * This method updates stewards attributes.
     *
     * @param steward to be modified.
     */
    public void updateSteward(Steward steward);

    /**
     * This methods returns steward from the database by his ID.
     *
     * @param id identifier of steward.
     * @return Instance of Steward if the id is in database, null otherwise.
     */
    public Steward getSteward(Long id);

    /**
     * This method returns all stewards stored in the database.
     *
     * @return List of all stewards currently stored in database.
     */
    public List<Steward> listAllStewards();
}
