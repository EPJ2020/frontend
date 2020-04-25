package com.example.lfg_source.entity;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GroupUnitTest {

    Group group = new Group();

    @Test
    public void setAndGetId() {
        group.setId(2);
        int id = group.getId();
        assertEquals(2, id);
    }

    @Test
    public void setAndGetName() {
        group.setName("Hungering");
        assertEquals("Hungering", group.getName());
    }


    @Test
    public void setAndGetActive(){
        group.setActive(true);
        assertTrue(group.getActive());
    }

    @Test
    public void setAndGetDescription(){
        group.setDescription("Gross, rund und schlau");
        assertEquals("Gross, rund und schlau", group.getDescription());
    }

    @Test
    public void setAndGetTags() {
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("C++");
        tags.add("Gross");
        tags.add("Stark");
        group.setTags(tags);
        assertEquals(tags, group.getTags());
    }

    @Test
    public void changeAttributes() {
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("C++");
        tags.add("Gross");
        tags.add("Stark");
        group.changeAttributes("lala",
                true,
                "MyGroup",
                tags);
        assertEquals(tags, group.getTags());
        assertEquals("lala", group.getDescription());
        assertTrue(group.getActive());
        assertEquals("MyGroup", group.getName());
    }


}