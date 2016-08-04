package com.github.jdsjlzx.interfaces;

public interface Openable {

    /**
     * The menu is open?
     *
     * @return true, otherwise false.
     */
    boolean isMenuOpen();

    /**
     * The menu is open on the left?
     *
     * @return true, otherwise false.
     */
    boolean isLeftMenuOpen();

    /**
     * The menu is open on the left?
     *
     * @return true, otherwise false.
     */
    boolean isLeftMenuOpenNotEqual();

    /**
     * The menu is open?
     *
     * @return true, otherwise false.
     */
    boolean isMenuOpenNotEqual();

    /**
     * The menu is open on the right?
     *
     * @return true, otherwise false.
     */
    boolean isRightMenuOpen();

    /**
     * The menu is open on the right?
     *
     * @return true, otherwise false.
     */
    boolean isRightMenuOpenNotEqual();

    /**
     * Open the current menu.
     */
    void smoothOpenMenu();

    /**
     * Open the menu on left.
     */
    void smoothOpenLeftMenu();

    /**
     * Open the menu on left for the duration.
     *
     * @param duration duration time.
     */
    void smoothOpenLeftMenu(int duration);

    /**
     * Open the menu on right.
     */
    void smoothOpenRightMenu();

    /**
     * Open the menu on right for the duration.
     *
     * @param duration duration time.
     */
    void smoothOpenRightMenu(int duration);

}
