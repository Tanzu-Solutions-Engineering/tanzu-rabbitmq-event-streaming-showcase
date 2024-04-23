using System.Text;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using Imani.Solutions.Core.API.Util;
using System.Data.Common;

var factory = new ConnectionFactory { Uri = new Uri("amqp://user:bitnami@localhost"), ClientProvidedName= "Receive"};
using var connection = factory.CreateConnection();
using var channel = connection.CreateModel();

var config = new ConfigSettings();

var queueName =  config.GetProperty("queue","hello-quorum");
var exchangeName = config.GetProperty("exchange","");
var queueType = config.GetProperty("queueType","quorum");
var routingKeyValue = config.GetProperty("routingKey","");
uint prefetchSize  = Convert.ToUInt32(config.GetPropertyInteger("prefetchSize",0));
ushort prefetchCount  = Convert.ToUInt16(config.GetPropertyInteger("prefetchCount",1));
bool autoAckFlag = config.GetPropertyBoolean("autoAck",true);

channel.QueueDeclare(queue: queueName,
                     durable: true,
                     exclusive: false,
                     autoDelete: false,
                     arguments: 
                     new Dictionary<string, object>()
                        {{ "x-queue-type", queueType}});

channel.BasicQos(prefetchSize: prefetchSize, prefetchCount: prefetchCount, global: false);

if(exchangeName.Length > 0 )
    channel.QueueBind(queue: queueName,
                  exchange: exchangeName,
                  routingKey: routingKeyValue);

Console.WriteLine(" [*] Waiting for messages.");

var consumer = new EventingBasicConsumer(channel);
consumer.Received += (model, ea) =>
{
    var body = ea.Body.ToArray();
    var message = Encoding.UTF8.GetString(body);
    Console.WriteLine($" [x] Received {message}");

    if(!autoAckFlag)
        channel.BasicAck(deliveryTag: ea.DeliveryTag, multiple: false);

};

channel.BasicConsume(queue: queueName,
                     autoAck: autoAckFlag,
                     consumer: consumer);

Console.WriteLine(" Press [enter] to exit.");
Console.ReadLine();