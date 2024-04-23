using System;
using System.Text;
using RabbitMQ.Client;


var factory = new ConnectionFactory { Uri = new Uri("amqp://user:bitnami@localhost"), ClientProvidedName = "Send"};
using var connection = factory.CreateConnection();
using var channel = connection.CreateModel();

var queueName = "hello-quorum";

channel.QueueDeclare(queue: queueName,
                     durable: true,
                     exclusive: false,
                     autoDelete: false,
                     arguments: 
                     new Dictionary<string, object>()
                        {{ "x-queue-type", "quorum"}});


const string message = "Hello World!";
var body = Encoding.UTF8.GetBytes(message);

channel.BasicPublish(exchange: string.Empty,
                     routingKey: queueName,
                     basicProperties: null,
                     body: body);
Console.WriteLine($" [x] Sent {message}");

Console.WriteLine(" Press [enter] to exit.");
Console.ReadLine();


