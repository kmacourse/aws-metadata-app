const { SNSClient, PublishCommand } = require("@aws-sdk/client-sns");

const TOPIC_ARN = "arn:aws:sns:us-west-1:522814711541:KMAEpamCourse-UploadsNotificationTopic";
const REGION = "us-west-1";

const snsClient = new SNSClient({ region: REGION });

exports.handler = async (event) => {
              if (!event.Records) {
                  return {
                      'statusCode': 200,
                      'body': JSON.stringify('No messages to process. Lambda function completed.')
                  };
              }

              console.log('Received event:', JSON.stringify(event, null, 2));
              
              for (const record of event.Records) {
                  try {
                      const params = {
                          Message: record.body,
                          TopicArn: TOPIC_ARN,
                          Subject: "Processed SQS Queue Message"
                      };
                      
                      const command = new PublishCommand(params);
                      await snsClient.send(command);
                      console.log('Message published to SNS:', record.body);
                  } catch (error) {
                      console.error('Error publishing to SNS:', error);
                      throw error;
                  }
              }
              
              return {
                  'statusCode': 200,
                  'body': JSON.stringify('Lambda function completed')
              };
          };