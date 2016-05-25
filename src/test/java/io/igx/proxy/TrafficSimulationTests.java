package io.igx.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

/**
 * @author Vinicius Carvalho
 */
public class TrafficSimulationTests {

	@Test
	public void runSimulation() throws Exception{
		TrafficSimulator simulator = new TrafficSimulator(100);
		simulator.connect("localhost",8008);
		simulator.run();
	}

	@Test
	public void simple() throws Exception {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup(1))
				.channel(NioSocketChannel.class)
				.handler(new TrafficGeneratorClientHandler());
		final CountDownLatch latch = new CountDownLatch(1);
		bootstrap.connect("localhost",8010).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()){
					future.channel().writeAndFlush(Unpooled.buffer().capacity(256).writeZero(256));
				}else {
					System.err.println("Connection attempt failed");
					future.cause().printStackTrace();
				}
				latch.countDown();
			}
		});

	latch.await();

	}

	public static class TrafficSimulator {

		private long runningTime;
		private Bootstrap bootstrap;
		private Channel channel;
		private Integer[] sizes = new Integer[]{256,512,1024,2048,4096,8192};
		private Random random = new Random();

		public TrafficSimulator(long runningTime) {
			this.runningTime = runningTime;
		}


		public void connect(String host, int port) throws Exception{
			this.bootstrap = new Bootstrap();
			final CountDownLatch latch = new CountDownLatch(1);
			bootstrap.group(new NioEventLoopGroup(1))
					.channel(NioSocketChannel.class)
					.handler(new TrafficGeneratorClientHandler());
				System.out.println("Creating connection");
				ChannelFuture cf = bootstrap.connect(host,port);
				cf.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						System.out.println("Connected");
						if (future.isSuccess()) {
							channel = future.channel();
						} else {
							future.cause().printStackTrace();
							future.channel().close();
							throw new RuntimeException(future.cause());
						}
						latch.countDown();
					}
				});
			latch.await();
			}


		public void run() throws Exception{
			Long stop = (runningTime*1000) + System.currentTimeMillis();
			while(stop - System.currentTimeMillis() > 0){
				int size = sizes[random.nextInt(sizes.length)];
				channel.writeAndFlush(Unpooled.buffer().capacity(size).writeZero(size));
				Thread.sleep(10L);
			}
		}


	}








}

