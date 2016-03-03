/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.email.queue.ecm.tests;

import java.util.ArrayList;
import java.util.List;

import org.everit.email.Email;
import org.everit.email.EmailAddress;
import org.everit.email.sender.EmailSender;
import org.everit.osgi.dev.testrunner.TestDuringDevelopment;
import org.everit.osgi.dev.testrunner.TestRunnerConstants;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.junit.Assert;
import org.junit.Test;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Test for Email Queue Component.
 */
@Component(componentId = "EmailQueueComponentTest")
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TESTRUNNER_ENGINE_TYPE,
        defaultValue = "junit4"),
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TEST_ID,
        defaultValue = "EmailQueueComponentTest") })
@Service(EmailQueueComponentTest.class)
public class EmailQueueComponentTest {

  private static final List<String> FROM_ADDRESSES = new ArrayList<>();

  private static final int N_5 = 5;

  private static final int N_7 = 7;

  static {
    FROM_ADDRESSES.add("firstFromEmailAddress");
    FROM_ADDRESSES.add("secondFromEmailAddress");
    FROM_ADDRESSES.add("thirdFromEmailAddress");
    FROM_ADDRESSES.add("forthFromEmailAddress");
    FROM_ADDRESSES.add("fifthFromEmailAddress");
    FROM_ADDRESSES.add("sixthFromEmailAddress");
    FROM_ADDRESSES.add("seventhFromEmailAddress");
  }

  private EmailSender emailSender;

  private Runnable passOnJob;

  private DummyEmailSender sinkEmailSender;

  private List<Email> assertSentMails(final int expectedSentMailSize) {
    List<Email> sentMail = sinkEmailSender.getSentMail();
    Assert.assertEquals(expectedSentMailSize, sentMail.size());

    int fromAddressesIndex = 0;
    for (Email email : sentMail) {
      Assert.assertEquals(FROM_ADDRESSES.get(fromAddressesIndex++), email.from.address);
    }
    return sentMail;
  }

  private Email createEmail(final String fromAddress) {
    Email email = new Email();
    email.withFrom(new EmailAddress()
        .withAddress(fromAddress));
    return email;
  }

  @ServiceRef(defaultValue = "")
  public void setEmailSender(final EmailSender emailSender) {
    this.emailSender = emailSender;
  }

  @ServiceRef(defaultValue = "")
  public void setPassOnJob(final Runnable passOnJob) {
    this.passOnJob = passOnJob;
  }

  @ServiceRef(defaultValue = "")
  public void setSinkEmailSender(final DummyEmailSender sinkEmailSender) {
    this.sinkEmailSender = sinkEmailSender;
  }

  @Test
  @TestDuringDevelopment
  public void testSendQueuedMail() {
    for (String fromAddress : FROM_ADDRESSES) {
      emailSender.sendEmail(createEmail(fromAddress));
    }

    assertSentMails(0);

    passOnJob.run();
    assertSentMails(N_5);

    passOnJob.run();
    assertSentMails(N_7);
  }
}
