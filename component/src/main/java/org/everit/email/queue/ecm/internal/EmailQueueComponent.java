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
package org.everit.email.queue.ecm.internal;

import java.util.Hashtable;

import org.everit.email.queue.CreatePassOnJobParam;
import org.everit.email.queue.EmailQueue;
import org.everit.email.queue.ecm.EmailQueueConstants;
import org.everit.email.sender.EmailSender;
import org.everit.email.store.EmailStore;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.IntegerAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.persistence.querydsl.support.QuerydslSupport;
import org.everit.transaction.propagator.TransactionPropagator;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * ECM component for {@link EmailQueue} implementation.
 */
@Component(componentId = EmailQueueConstants.SERVICE_PID,
    configurationPolicy = ConfigurationPolicy.OPTIONAL,
    label = "Everit Email Queue Component",
    description = "ECM component for Email Queue.")
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = EmailQueueConstants.DEFAULT_SERVICE_DESCRIPTION,
        priority = EmailQueueAttributePriority.P_SERVICE_DESCRIPTION,
        label = "Service Description",
        description = "The description of this component configuration."
            + "It is used to easily identify the service registered by this component.") })
@ManualService(EmailStore.class)
public class EmailQueueComponent {

  private ServiceRegistration<Runnable> createPassOnJobServiceRegistration;

  private EmailSender emailSender;

  private ServiceRegistration<EmailSender> emailSenderServiceRegistration;

  private EmailStore emailStore;

  private int max;

  private QuerydslSupport querydslSupport;

  private TransactionPropagator transactionPropagator;

  /**
   * The activate method that registers a {@link EmailSender} and {@link Runnable} OSGi service.
   */
  @Activate
  public void activate(final ComponentContext<EmailQueueComponent> componentContext) {
    EmailQueue emailQueue =
        new EmailQueue(emailSender, emailStore, querydslSupport, transactionPropagator);
    Hashtable<String, Object> properties = new Hashtable<>(componentContext.getProperties());
    emailSenderServiceRegistration = componentContext.registerService(
        EmailSender.class,
        emailQueue,
        properties);

    CreatePassOnJobParam param = new CreatePassOnJobParam()
        .max(max);
    Runnable createPassOnJob = emailQueue.createPassOnJob(param);
    createPassOnJobServiceRegistration = componentContext.registerService(
        Runnable.class,
        createPassOnJob,
        properties);
  }

  /**
   * Unregisters the {@link EmailSender} and {@link Runnable} OSGi service.
   */
  @Deactivate
  public void deactivate() {
    if (emailSenderServiceRegistration != null) {
      emailSenderServiceRegistration.unregister();
    }

    if (createPassOnJobServiceRegistration != null) {
      createPassOnJobServiceRegistration.unregister();
    }
  }

  @ServiceRef(attributeId = EmailQueueConstants.ATTR_EMAIL_SENDER, defaultValue = "",
      attributePriority = EmailQueueAttributePriority.P_EMAIL_SENDER, label = "EmailSender",
      description = "OSGi service filter for org.everit.email.sender.EmailSender.")
  public void setEmailSender(final EmailSender emailSender) {
    this.emailSender = emailSender;
  }

  @ServiceRef(attributeId = EmailQueueConstants.ATTR_EMAIL_STORE, defaultValue = "",
      attributePriority = EmailQueueAttributePriority.P_EMAIL_STORE, label = "EmailStore",
      description = "OSGi service filter for org.everit.email.store.EmailStore.")
  public void setEmailStore(final EmailStore emailStore) {
    this.emailStore = emailStore;
  }

  @IntegerAttribute(attributeId = EmailQueueConstants.ATTR_MAX,
      defaultValue = EmailQueueConstants.DEFAULT_MAX,
      priority = EmailQueueAttributePriority.P_MAX, label = "Max",
      description = "The maximum number that want to send at the same time.")
  public void setMax(final int max) {
    this.max = max;
  }

  @ServiceRef(attributeId = EmailQueueConstants.ATTR_QUERYDSL_SUPPORT, defaultValue = "",
      attributePriority = EmailQueueAttributePriority.P_QUERYDSL_SUPPORT,
      label = "QuerydslSupport",
      description = "OSGi service filter for "
          + "org.everit.persistence.querydsl.support.QuerydslSupport.")
  public void setQuerydslSupport(final QuerydslSupport querydslSupport) {
    this.querydslSupport = querydslSupport;
  }

  @ServiceRef(attributeId = EmailQueueConstants.ATTR_TRANSACTION_PROPAGATOR, defaultValue = "",
      attributePriority = EmailQueueAttributePriority.P_TRANSACTION_PROPAGATOR,
      label = "TransactionPropagator",
      description = "OSGi service filter for "
          + "org.everit.transaction.propagator.TransactionPropagator.")
  public void setTransactionPropagator(final TransactionPropagator transactionPropagator) {
    this.transactionPropagator = transactionPropagator;
  }
}
