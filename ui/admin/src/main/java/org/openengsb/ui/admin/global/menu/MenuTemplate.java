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

package org.openengsb.ui.admin.global.menu;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.openengsb.core.api.security.model.SecurityAttributeEntry;
import org.openengsb.core.common.SecurityAttributeProviderImpl;
import org.openengsb.ui.admin.global.BookmarkablePageLabelLink;
import org.openengsb.ui.admin.index.Index;
import org.openengsb.ui.admin.sendEventPage.SendEventPage;
import org.openengsb.ui.admin.serviceListPage.ServiceListPage;
import org.openengsb.ui.admin.taskOverview.TaskOverview;
import org.openengsb.ui.admin.testClient.TestClient;
import org.openengsb.ui.admin.userService.UserListPage;
import org.openengsb.ui.admin.wiringPage.WiringPage;
import org.openengsb.ui.admin.workflowEditor.WorkflowEditor;
import org.ops4j.pax.wicket.api.PaxWicketBean;

@SuppressWarnings("serial")
public class MenuTemplate extends Panel {
	
	private final ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
    private final ArrayList<String> avialableItems = new ArrayList<String>();
    
    private static String menuIndex;
    
    @PaxWicketBean(name = "attributeStore")
    private SecurityAttributeProviderImpl attributeStore;


    public MenuTemplate(String id, String menuIndex) {
        super(id);
        
        MenuTemplate.menuIndex = menuIndex;
        initMainMenuItems();
        initMainMenu();
    }
	
    private void initMainMenuItems() {
    	addMenuItem("Index", Index.class, "index.title",new ResourceReference(MenuTemplate.class,"dashboardIcon.png"));
    	addMenuItem("UserService", UserListPage.class, "userService.title", new ResourceReference(MenuTemplate.class,"userIcon.png"), "ROLE_ADMIN");
        addMenuItem("TestClient", TestClient.class, "testclient.title",new ResourceReference(MenuTemplate.class,"testClientIcon.png"));
        addMenuItem("SendEventPage", SendEventPage.class, "sendevent.title",new ResourceReference(MenuTemplate.class,"dashboardIcon.png"));
        addMenuItem("ServiceListPage", ServiceListPage.class, "serviceList.title",new ResourceReference(MenuTemplate.class,"dashboardIcon.png"));
        addMenuItem("TaskOverview", TaskOverview.class, "taskOverview.title",new ResourceReference(MenuTemplate.class,"dashboardIcon.png"));
        addMenuItem("WorkflowEditor", WorkflowEditor.class, "workflowEditor.title", new ResourceReference(MenuTemplate.class,"dashboardIcon.png"));
        addMenuItem("WiringPage", WiringPage.class, "wiring.title", new ResourceReference(MenuTemplate.class,"dashboardIcon.png"), "ROLE_ADMIN");
    }

    private void initMainMenu() {
    	
        if (MenuTemplate.getActiveIndex() == null || !avialableItems.contains(MenuTemplate.getActiveIndex())) {
            // update menu item to index, because page index is not found!
            MenuTemplate.menuIndex = "Index";
        }
    	
    	// generate main navigation
        ListView<MenuItem>MenuItems = new ListView<MenuItem>("menuItems", menuItems) {
            @Override
            protected void populateItem(ListItem<MenuItem> item) {
                MenuItem menuItem = item.getModelObject();
                item.add(menuItem.getLink());
                
                String backgroundAttribute = "background:url('resources/"+menuItem.getIcon().getSharedResourceKey()+"') no-repeat scroll left center transparent;";
                item.add(new SimpleAttributeModifier("style",backgroundAttribute));
                
                if(item.getIndex()==menuItems.size()-1) {
                	item.add(new SimpleAttributeModifier("class","lastElement"));
                }
                
                // set menu item to active
                if (menuItem.getItemName().equals(MenuTemplate.getActiveIndex())) {
                    
                	item.add(new SimpleAttributeModifier("class","activeElement"));
                   
                }
            }
        };
        add(MenuItems);
    }
    
    /**
     * get the name of the current active menu item
     */
    public static String getActiveIndex() {
        return MenuTemplate.menuIndex;
    }
    
    /**
     * Adds a new item to main header navigation where the index defines the name of the index, which should be the
     * class name; linkClass defines the class name to be linked to; langKey defines the language key for the text which
     * should be displayed and authority defines who is authorized to see the link
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addMenuItem(String index, Class<? extends WebPage> linkClass, String langKey, ResourceReference icon,
            String... authority) {
		StringResourceModel label = new StringResourceModel(langKey, this, null);
	    BookmarkablePageLabelLink pageLabelLink = new BookmarkablePageLabelLink("link", linkClass, label);
	    addAuthorizationRoles(pageLabelLink, authority);
	    menuItems.add(new MenuItem(index, pageLabelLink, icon));
	    avialableItems.add(index);
	}
	
    private void addAuthorizationRoles(BookmarkablePageLabelLink<?> pageLabelLink, String... authority) {
        if (authority == null) {
            return;
        }
        for (String a : authority) {
            attributeStore.putAttribute(pageLabelLink, new SecurityAttributeEntry(a, "RENDER"));
        }
    }
	
	private static class MenuItem implements Serializable {
        private final String index;
        private final BookmarkablePageLabelLink<? extends WebPage> link;
        private final ResourceReference icon;

        @SuppressWarnings("unused")
		public MenuItem(String index, BookmarkablePageLabelLink<? extends WebPage> link) {
            this.index = index;
            this.link = link;
            icon=null;
        }
        
        public MenuItem(String index, BookmarkablePageLabelLink<? extends WebPage> link, ResourceReference icon) {
            this.index = index;
            this.link = link;
            this.icon = icon;
        }

        public String getItemName() {
            return index;
        }

        public BookmarkablePageLabelLink<? extends WebPage> getLink() {
            return link;
        }
        
        public ResourceReference getIcon() {
            return icon;
        }
    } 

}