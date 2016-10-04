##########################################################
# Dockerfile that provides the image for Stunner Showcase
##########################################################

####### BASE ############
FROM jboss/wildfly:10.0.0.Final

####### MAINTAINER ############
MAINTAINER "Roger" "romartin@redhat.com"

####### ENVIRONMENT ############
ENV JBOSS_BIND_ADDRESS 0.0.0.0
ENV KIE_ARTIFACT wirez
ENV KIE_VERSION 0.5.0-SNAPSHOT
ENV KIE_CLASSIFIER wildfly10
ENV KIE_SERVER_PROFILE standalone-full-stunner
ENV JAVA_OPTS -XX:MaxPermSize=256m -Xms256m -Xmx512m

####### STUFF ############
USER root

ADD bin/$KIE_ARTIFACT-$KIE_VERSION-$KIE_CLASSIFIER.war $JBOSS_HOME/standalone/deployments/wirez.war
RUN touch $JBOSS_HOME/standalone/deployments/wirez.war.dodeploy
ADD etc/standalone-full-stunner.xml $JBOSS_HOME/standalone/configuration/standalone-full-stunner.xml
ADD etc/stunner-users.properties $JBOSS_HOME/standalone/configuration/stunner-users.properties
ADD etc/stunner-roles.properties $JBOSS_HOME/standalone/configuration/stunner-roles.properties
ADD etc/start.sh $JBOSS_HOME/bin/start.sh

RUN chown jboss:jboss $JBOSS_HOME/standalone/configuration/standalone-full-stunner.xml && \
chown jboss:jboss $JBOSS_HOME/standalone/configuration/stunner-users.properties && \
chown jboss:jboss $JBOSS_HOME/standalone/configuration/stunner-roles.properties && \
chown jboss:jboss $JBOSS_HOME/bin/start.sh && \
chown -R jboss:jboss $JBOSS_HOME/standalone/deployments/wirez.war && \
chown jboss:jboss $JBOSS_HOME/standalone/deployments/wirez.war.dodeploy

####### CUSTOM JBOSS USER ############
# Switchback to jboss user
USER jboss

####### EXPOSE INTERNAL VFS GIT PORT ############
EXPOSE 8001

####### RUNNING STUNNER ############
WORKDIR $JBOSS_HOME/bin/
CMD ["./start.sh"]