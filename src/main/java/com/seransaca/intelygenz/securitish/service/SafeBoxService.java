package com.seransaca.intelygenz.securitish.service;

import com.seransaca.intelygenz.securitish.entity.SafeBox;

import java.util.List;
import java.util.Optional;

public interface SafeBoxService {

    /**
     * Return a safebox when is created.
     *
     * @return A safebox
     */
    public SafeBox createNewSafeBox(String name, String password);

    /**
     * Return a safebox with the same name and password.
     *
     * @return A safebox
     */
    public List<SafeBox> findSafeBox(String name, String password);

    /**
     * Return a safebox with the same name and password.
     *
     * @return A safebox
     */
    public SafeBox findSafeBox(String uuid);

    /**
     * Update safebox
     */
    public void updateSafeBox(SafeBox safeBox);
}
