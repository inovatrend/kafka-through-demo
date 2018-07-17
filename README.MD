**Testing KStream.through operation behaviour**

This is small app demonstrating behaviour of streaming application when KStream.through operation is used and target topic is used by another streaming application or producer.

There are three components:

_InputProducer_ - produces random messages and  writes them into topic called  input-topic

_StreamingApp_ - streaming application that uses input-topic as source, transforms the message, writes it to topic called "side-topic", then writes the message to system output, and finally writes it to topic called output-topic

_IntermediateProducer_ - this is the "bad guy". It writes messages directly to side-topic causing unexpected behavior of StreamingApp component


**Prerequisites**

Zookeeper and Kafka servers installed and ready for use  
Topics created: "input-topic", "side-topic", "output-topic"


**Usage**

1. Start InputProducer and let it run
2. Start StreamingApp and observe it's output. You should something like this:

    _Message after through: MESSAGE NO 1 PRODUCED AT 14:20:40.990  
    Message read from input topic: Message no 1 produced at 14:20:41.091_
    
3. Stop InputProducer and wait until StreamingApp's output stops (this means streaming app consumed all messages InputProducer produced).

4. Start IntermediateProducer and observe StreamingApp's output again.  
    You will notice something like this:
    
    _Message after through: This message is not from input-topic.  14:27:03.898  
    Message after through: This message is not from input-topic.  14:27:03.999  
    Message after through: This message is not from input-topic.  14:27:04.099_
 


