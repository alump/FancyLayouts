/**
 * FancyImageState.java (FancyLayouts)
 * 
 * Copyright 2012 Vaadin Ltd, Sami Viitanen <alump@vaadin.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vaadin.alump.fancylayouts.gwt.client.shared;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ComponentState;
import com.vaadin.shared.communication.URLReference;

@SuppressWarnings("serial")
public class FancyImageState extends ComponentState {

    public enum Transition {
        FADE, FADE_AND_ROTATE;
    }

    public int timeoutMs = 2000;

    public boolean autoBrowse = false;

    public List<URLReference> images = new ArrayList<URLReference>();

    public Transition transition = Transition.FADE;

}