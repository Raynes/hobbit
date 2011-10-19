# Hobbit

This is a polymorphic library for interacting with various URL shortening services.

The library consists of several API implementations and a protocol called `Shortener` that has two methods, `shorten` and `expand`. Each service API implementation implements Shortener but may also have other functions for less common and/or unique API calls.

This library is currently in development and only [Bitly](bitly.com) is (mostly) done at the moment. An arbitrary number of other implementations are coming.
