/**
 * Licensed to the Austrian Association for Software Tool Integration (AASTI)
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. The AASTI licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openengsb.core.common.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.openengsb.core.api.model.OpenEngSBModelEntry;

public class ModelUtilsTest {

    @Test
    public void testSetterAndGetterWithStringObject_shouldWork() {
        TestModel model = ModelUtils.createEmptyModelObject(TestModel.class);
        model.setId("testId");

        assertThat(model.getId(), is("testId"));
    }

    @Test
    public void testSetterAndGetterWithDateObject_shouldWork() {
        TestModel model = ModelUtils.createEmptyModelObject(TestModel.class);
        Date date = new Date();
        model.setDate(date);

        assertThat(model.getDate(), is(date));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotGetterOrSetterMethod_shouldThrowException() {
        TestModel model = ModelUtils.createEmptyModelObject(TestModel.class);
        model.testMethod();
    }

    @Test
    public void testGetOpenEngSBModelEntries_shouldWork() {
        TestModel model = ModelUtils.createEmptyModelObject(TestModel.class);
        String id = "testId";
        model.setId(id);
        List<OpenEngSBModelEntry> entries = model.getOpenEngSBModelEntries();

        boolean idExisting = false;
        String tempId = null;

        for (OpenEngSBModelEntry entry : entries) {
            if (entry.getKey().equals("id")) {
                idExisting = true;
                tempId = (String) entry.getValue();
            }
        }
        assertThat(idExisting, is(true));
        assertThat(tempId, is(id));
    }

    @Test
    public void testGetOpenEngSBModelEntriesNonSimpleObject_shouldWork() {
        TestModel model = ModelUtils.createEmptyModelObject(TestModel.class);
        Date date = new Date();
        model.setDate(date);

        boolean dateExisting = false;
        Date tempDate = null;

        for (OpenEngSBModelEntry entry : model.getOpenEngSBModelEntries()) {
            if (entry.getKey().equals("date")) {
                dateExisting = true;
                tempDate = (Date) entry.getValue();
            }
        }

        assertThat(dateExisting, is(true));
        assertThat(tempDate, is(date));
    }

    @Test
    public void testGetOpenEngSBModelEntriesListObjects_shouldWork() {
        List<String> test = Arrays.asList("string1", "string2", "string3");
        OpenEngSBModelEntry entry = new OpenEngSBModelEntry("list", test, test.getClass());
        TestModel model = ModelUtils.createEmptyModelObject(TestModel.class, entry);

        boolean stringEntry1 = false;
        boolean stringEntry2 = false;
        boolean stringEntry3 = false;

        for (OpenEngSBModelEntry e : model.getOpenEngSBModelEntries()) {
            if (e.getKey().equals("list")) {
                @SuppressWarnings("unchecked")
                List<String> list = (List<String>) e.getValue();
                for (String elem : list) {
                    if (elem.equals("string1")) {
                        stringEntry1 = true;
                    } else if (elem.equals("string2")) {
                        stringEntry2 = true;
                    } else if (elem.equals("string3")) {
                        stringEntry3 = true;
                    }
                }
            }
        }

        assertThat(stringEntry1, is(true));
        assertThat(stringEntry2, is(true));
        assertThat(stringEntry3, is(true));
    }

    @Test
    public void testGetOpenEngSBModelEntriesEnumObjects_shouldWork() {
        ENUM enumeration = ENUM.A;
        OpenEngSBModelEntry entry = new OpenEngSBModelEntry("enumeration", enumeration, ENUM.class);
        TestModel model = ModelUtils.createEmptyModelObject(TestModel.class, entry);

        boolean enumEntry = false;

        for (OpenEngSBModelEntry e : model.getOpenEngSBModelEntries()) {
            if (e.getKey().equals("enumeration") && e.getValue().equals(enumeration)) {
                enumEntry = true;
            }
        }

        assertThat(enumEntry, is(true));
    }

    @Test
    public void testGetOpenEngSBModelEntriesWhichWerentSettet_shouldWork() {
        TestModel model = ModelUtils.createEmptyModelObject(TestModel.class);

        List<OpenEngSBModelEntry> entries = model.getOpenEngSBModelEntries();

        // 8 because the model define 8 fields
        assertThat(entries.size(), is(8));
    }

    @Test
    public void testFunctionalityOfAddingTailInformation_shouldWork() {
        TestModel model = ModelUtils.createEmptyModelObject(TestModel.class);

        model.addOpenEngSBModelEntry(new OpenEngSBModelEntry("tailentry", "tail", String.class));
        boolean tailEntry = false;

        for (OpenEngSBModelEntry e : model.getOpenEngSBModelEntries()) {
            if (e.getKey().equals("tailentry") && e.getValue().equals("tail")) {
                tailEntry = true;
            }
        }
        assertThat(tailEntry, is(true));
    }

    @Test
    public void testFunctionalityOfRemovingTailInformation_shouldWork() {
        TestModel model = ModelUtils.createEmptyModelObject(TestModel.class);

        model.addOpenEngSBModelEntry(new OpenEngSBModelEntry("tailentry", "tail", String.class));
        boolean tailEntry = false;

        for (OpenEngSBModelEntry e : model.getOpenEngSBModelEntries()) {
            if (e.getKey().equals("tailentry") && e.getValue().equals("tail")) {
                tailEntry = true;
            }
        }

        model.removeOpenEngSBModelEntry("tailentry");

        boolean tailAway = true;

        for (OpenEngSBModelEntry e : model.getOpenEngSBModelEntries()) {
            if (e.getKey().equals("tailentry") && e.getValue().equals("tail")) {
                tailEntry = false;
            }
        }

        assertThat(tailEntry, is(true));
        assertThat(tailAway, is(true));
    }

}