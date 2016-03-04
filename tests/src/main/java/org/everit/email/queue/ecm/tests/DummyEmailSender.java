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
import org.everit.email.sender.BulkEmailSender;
import org.everit.email.sender.EmailSender;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Dummy Email Sender implementation.
 */
@Component(configurationPolicy = ConfigurationPolicy.IGNORE)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@Service(value = { EmailSender.class, DummyEmailSender.class })
public class DummyEmailSender implements EmailSender {

  /**
   * Dummy Bulk Email Sender implementation.
   */
  private class DummyBulkEmailSender implements BulkEmailSender {

    @Override
    public void close() {
      // do nothing
    }

    @Override
    public void sendEmail(final Email mail) {
      sentMail.add(mail);
    }

  }

  private List<Email> sentMail = new ArrayList<>();

  public List<Email> getSentMail() {
    return sentMail;
  }

  @Override
  public BulkEmailSender openBulkEmailSender() {
    return new DummyBulkEmailSender();
  }

  @Override
  public void sendEmail(final Email mail) {
    openBulkEmailSender().sendEmail(mail);
  }

}
