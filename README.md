AWARE Plugin: ESM Flexible
==========================

This plugin allows you to configure ESM questionnaires from a configuration file retrieved either from the own APK or from a remote location. The configuration file is written in XML and structured following the ESM_Definition schema.

The ESM card enables manual trigger of a selected questionnaire.

**Multiple questionnaires** 
- The URL of the configuration file is specified in the settings of the plugin. Several URLs of local/remote files can be specified as comma separated values.

**Real time schedule update**
- Each time the plugin activity is started, the questionnaires are retrieved and the schedules are updated.

# Settings
Parameters adjustable on the dashboard and client:
- **status_plugin_esm_flexible**: (boolean) activate/deactivate plugin
- **questionnaire_names**: (string) comma separated names/URLs of the XML configuration files
- **manual_trigger_questionnaire**: (string) name of questionnaire enabled for manual trigger
