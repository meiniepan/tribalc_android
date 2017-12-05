package com.gs.buluo.app.utils;

import com.gs.buluo.app.bean.ContactsPersonEntity;

import java.util.Comparator;

/**
 * Created by Solang on 2017/12/5.
 */

public class PinyinComparator implements Comparator<ContactsPersonEntity> {

    public int compare(ContactsPersonEntity o1, ContactsPersonEntity o2) {
        if (o1.sortLetters.equals("@") || o2.sortLetters.equals("#")) {
            return -1;
        } else if (o1.sortLetters.equals("#") || o2.sortLetters.equals("@")) {
            return 1;
        } else {
            return o1.sortLetters.compareTo(o2.sortLetters);
        }
    }

}
