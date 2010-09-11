/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.cdi.translate;

import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Arquillian.class)
public class TranslateTestCase {

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar")
				.addPackage(
						TranslateController.class.getPackage())
				.addManifestResource(
						new ByteArrayAsset("<beans/>".getBytes()),
						ArchivePaths.create("beans.xml"));
	}
	
	@Inject TranslateController controller;

	@Test // https://jira.jboss.org/jira/browse/ARQ-55
	public void shouldSetInputText() throws Exception 
	{
	   controller.setInputText("hi");
	}
	
	@Test(dependsOnMethods = "shouldSetInputText") // https://jira.jboss.org/jira/browse/ARQ-55
	public void shouldTranslate() throws Exception 
	{
		controller.translate();
	}
	
	@Test(dependsOnMethods = "shouldTranslate") // https://jira.jboss.org/jira/browse/ARQ-55
	public void shouldVerify() throws Exception 
	{
		Assert.assertEquals(controller.getTranslation(), "hei");
	}
}
