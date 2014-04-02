package com.droid.ndege.tests;

import android.test.ActivityTestCase;

/**
 * Created by james on 11/03/14.
 */
public class SampleTests extends ActivityTestCase{

    public SampleTests(){}

    @Override
    protected void setUp() throws Exception{
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception{
        super.tearDown();
    }

    public void sampleTesting(){
        fail("passing tests...");
    }
}
