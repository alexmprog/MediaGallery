package com.renovavision.mediagallery.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils {

    public static void commitFragment(FragmentManager fragmentManager, int containerId,
                                      Fragment fragment, boolean addToBackStack) {
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        final String tag = fragment.getClass().getSimpleName();
        transaction.replace(containerId, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    public static void addFragment(FragmentManager fragmentManager, int containerId,
                                   Fragment fragment, boolean addToBackStack) {
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        final String tag = fragment.getClass().getSimpleName();
        transaction.add(containerId, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }


    public static Fragment getFragmentByTag(FragmentManager fragmentManager, Class<?> fragment) {
        return fragmentManager.findFragmentByTag(fragment.getSimpleName());
    }

}
