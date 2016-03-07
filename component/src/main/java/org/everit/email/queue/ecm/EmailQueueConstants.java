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
package org.everit.email.queue.ecm;

/**
 * Constants of the Email Store component.
 */
public final class EmailQueueConstants {

  public static final String ATTR_BATCH_MAX_SIZE = "batchMaxSize";

  public static final String ATTR_EMAIL_SENDER = "emailSender.target";

  public static final String ATTR_EMAIL_STORE = "emailStore.target";

  public static final String ATTR_QUERYDSL_SUPPORT = "querydslSupport.target";

  public static final String ATTR_TRANSACTION_PROPAGATOR = "transactionPropagator.target";

  public static final int DEFAULT_BATCH_MAX_SIZE = 10;

  public static final String DEFAULT_SERVICE_DESCRIPTION = "Email Queue Component";

  public static final String SERVICE_PID = "org.everit.email.queue.ecm.EmailQueue";

  private EmailQueueConstants() {
  }
}
