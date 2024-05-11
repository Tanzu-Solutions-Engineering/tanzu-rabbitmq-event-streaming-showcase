using System.Security.AccessControl;
using System.Text;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using Imani.Solutions.Core.API.Util;


//Get Settings
var config = new ConfigSettings();
var clientName = config.GetProperty("clientName","Receive");
var queueName =  config.GetProperty("queue","hello-quorum");
var exchangeName = config.GetProperty("exchange","");
var queueType = config.GetProperty("queueType","quorum");
var routingKeyValue = config.GetProperty("routingKey","");
uint prefetchSize  = Convert.ToUInt32(config.GetPropertyInteger("prefetchSize",0));
ushort prefetchCount  = Convert.ToUInt16(config.GetPropertyInteger("prefetchCount",1));
bool autoAckFlag = config.GetPropertyBoolean("autoAck",true);
var streamOffset = config.GetProperty("streamOffset","last");
var consumerArguments =  new Dictionary<string, object>();

if("stream".Equals(queueType))
{
    consumerArguments.Add("x-stream-offset", streamOffset);

}

//Make connection

var factory = new ConnectionFactory { Uri = new Uri("amqp://user:bitnami@localhost"), ClientProvidedName= clientName};
using var connection = factory.CreateConnection();
using var channel = connection.CreateModel();

//Declare Queue
channel.QueueDeclare(queue: queueName,
                     durable: true,
                     exclusive: false,
                     autoDelete: false,
                     arguments: 
                     new Dictionary<string, object>()
                        {{ "x-queue-type", queueType}});

//Declare Qualify of service consumption
channel.BasicQos(prefetchSize: prefetchSize, prefetchCount: prefetchCount, global: false);

//Default Exchange Binding rules
if(exchangeName.Length > 0 )
    channel.QueueBind(queue: queueName,
                  exchange: exchangeName,
                  routingKey: routingKeyValue);


Console.WriteLine(" [*] Waiting for messages.");

//Define Consumer logic
var consumer = new EventingBasicConsumer(channel);
consumer.Received += (model, ea) =>
{
    var body = ea.Body.ToArray();
    var message = Encoding.UTF8.GetString(body);
    Console.WriteLine($" [x] Received {message}");

    if(!autoAckFlag)
        channel.BasicAck(deliveryTag: ea.DeliveryTag, multiple: false);

};


channel.BasicConsume(
                    queue: queueName,
                     autoAck: autoAckFlag,
                     consumer: consumer,
                     arguments:  consumerArguments);


Console.WriteLine(" Press [enter] to exit.");
Console.ReadLine();