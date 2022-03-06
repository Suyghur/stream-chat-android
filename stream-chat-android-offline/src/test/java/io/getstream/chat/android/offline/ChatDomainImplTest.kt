package io.getstream.chat.android.offline

// @ExperimentalCoroutinesApi
// Todo: Move those tests to proper class

// internal class ChatDomainImplTest {
//
//     companion object {
//         @JvmField
//         @RegisterExtension
//         val testCoroutines = TestCoroutineExtension()
//     }
//
//     @Test
//     fun `Given a sync needed message with uploaded attachment Should perform retry correctly`() {
//         testCoroutines.scope.runBlockingTest {
//             val syncNeededMessageWithSuccessAttachment = randomMessage(
//                 syncStatus = SyncStatus.SYNC_NEEDED,
//                 attachments = mutableListOf(randomAttachment { uploadState = Attachment.UploadState.Success }),
//             )
//             val client = mock<ChatClient> {
//                 on { it.channel(any()) } doAnswer {
//                     mock {
//                         on { deleteMessage(any(), any()) } doAnswer {
//                             TestCall(Result.success(syncNeededMessageWithSuccessAttachment))
//                         }
//                     }
//                 }
//             }
//             val repositoryFacade = mock<RepositoryFacade> {
//                 onBlocking { selectMessageBySyncState(SyncStatus.SYNC_NEEDED) } doReturn listOf(syncNeededMessageWithSuccessAttachment)
//                 onBlocking { selectMessageBySyncState(SyncStatus.AWAITING_ATTACHMENTS) } doReturn emptyList()
//             }
//             val sut = Fixture(client)
//                 .withRepositoryFacade(repositoryFacade)
//                 .withActiveChannel(cid = syncNeededMessageWithSuccessAttachment.cid, channelController = mock())
//                 .get()
//
//             val result = sut.retryMessages()
//
//             result.size shouldBeEqualTo 1
//             result.first() shouldBeEqualTo syncNeededMessageWithSuccessAttachment
//         }
//     }
//
//     @Test
//     fun `Given an awaiting attachments message Should perform retry correctly`() {
//         testCoroutines.scope.runBlockingTest {
//             val awaitingAttachmentsMessage = randomMessage(
//                 syncStatus = SyncStatus.AWAITING_ATTACHMENTS,
//                 attachments = mutableListOf(
//                     randomAttachment {
//                         uploadState =
//                             Attachment.UploadState.InProgress(positiveRandomLong(20), positiveRandomLong(100) + 20)
//                     },
//                     randomAttachment { uploadState = Attachment.UploadState.Success },
//                 ),
//             )
//             val repositoryFacade = mock<RepositoryFacade> {
//                 onBlocking { selectMessageBySyncState(SyncStatus.SYNC_NEEDED) } doReturn emptyList()
//                 onBlocking {
//                     selectMessageBySyncState(SyncStatus.AWAITING_ATTACHMENTS)
//                 } doReturn listOf(awaitingAttachmentsMessage)
//             }
//             val sut = Fixture()
//                 .withRepositoryFacade(repositoryFacade)
//                 .withActiveChannel(cid = awaitingAttachmentsMessage.cid, channelController = mock())
//                 .get()
//
//             val result = sut.retryMessages()
//
//             result.size shouldBeEqualTo 1
//             result.first() shouldBeEqualTo awaitingAttachmentsMessage
//         }
//     }
//
//     @Test
//     fun `Given a message without attachments Should perform retry correctly`() {
//         testCoroutines.scope.runBlockingTest {
//             val message = randomMessage(syncStatus = SyncStatus.SYNC_NEEDED)
//             val client = mock<ChatClient> {
//                 on { it.channel(any()) } doAnswer {
//                     mock {
//                         on { deleteMessage(any(), any()) } doAnswer {
//                             TestCall(Result.success(message))
//                         }
//                     }
//                 }
//             }
//             val repositoryFacade = mock<RepositoryFacade> {
//                 onBlocking { selectMessageBySyncState(SyncStatus.SYNC_NEEDED) } doReturn listOf(message)
//                 onBlocking { selectMessageBySyncState(SyncStatus.AWAITING_ATTACHMENTS) } doReturn emptyList()
//             }
//             val sut = Fixture(client)
//                 .withRepositoryFacade(repositoryFacade)
//                 .withActiveChannel(cid = message.cid, channelController = mock())
//                 .get()
//
//             val result = sut.retryMessages()
//
//             result.size shouldBeEqualTo 1
//             result.first() shouldBeEqualTo message
//         }
//     }
//
//     private class Fixture(client: ChatClient = mock { on { it.channel(any()) } doReturn mock() }) {
//         private val db: ChatDatabase = mock {
//             on { userDao() } doReturn mock()
//             on { channelConfigDao() } doReturn mock()
//             on { channelStateDao() } doReturn mock()
//             on { queryChannelsDao() } doReturn mock()
//             on { messageDao() } doReturn mock()
//             on { reactionDao() } doReturn mock()
//             on { syncStateDao() } doReturn mock()
//             on { attachmentDao() } doReturn mock()
//         }
//         private val handler: Handler = mock()
//         private val offlineEnabled = true
//         private val userPresence = true
//         private val recoveryEnabled = true
//
//         private val chatDomainImpl = ChatDomain.Builder(mock(), client)
//             .handler(handler)
//             .userPresenceEnabled()
//             .recoveryEnabled()
//             .build()
//             .let { it as ChatDomainImpl }
//             .also {
//                 val user = randomUser()
//                 it.repos = RepositoryFacade.create(RepositoryFactory(db, user), mock(), mock())
//                 it.setUser(user)
//                 it.userConnected(user)
//             }
//
//         fun withRepositoryFacade(repositoryFacade: RepositoryFacade) = apply {
//             chatDomainImpl.repos = repositoryFacade
//         }
//
//         fun withActiveChannel(cid: String, channelController: ChannelController) = apply {
//             chatDomainImpl.addActiveChannel(cid = cid, channelController = channelController)
//         }
//
//         fun get(): ChatDomainImpl = chatDomainImpl
//     }
// }
