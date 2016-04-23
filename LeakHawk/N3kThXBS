<?php

use Behat\Behat\Context\ClosuredContextInterface,
    Behat\Behat\Context\TranslatedContextInterface,
    Behat\Behat\Context\BehatContext,
    Behat\Behat\Exception\PendingException;
use Behat\Gherkin\Node\PyStringNode,
    Behat\Gherkin\Node\TableNode;
 use Behat\Behat\Context\Context;
 use Symfony\Component\HttpFoundation\Session\Session;

use Behat\MinkExtension\Context\MinkContext;
use Behat\Symfony2Extension\Context\KernelAwareContext;
use Symfony\Component\HttpKernel\KernelInterface;

//
// Require 3rd-party libraries here:
//
//   require_once 'PHPUnit/Autoload.php';
//   require_once 'PHPUnit/Framework/Assert/Functions.php';
//

/**
 * Features context.
 */
class FeatureContext extends MinkContext implements KernelAwareContext
{
    /**
     * @var \Symfony\Component\HttpKernel\KernelInterface $kernel
     */
    private $kernel = null; 
    /**
     * Initializes context.
     * Every scenario gets its own context object.
     *
     * @param array $parameters context parameters (set them up through behat.yml)
     */
    public function __construct(Session $session, $simpleArg)
     {
         
     }
    /**
    * @Then /^I wait$/
    */
   public function iWaitForTheSuggestionBoxToAppear()
   {
       $this->getSession()->wait(5000);
   }

    /**
     * @param \Symfony\Component\HttpKernel\KernelInterface $kernel
     *
     * @return null
     */
    public function setKernel(KernelInterface $kernel)
    {
        $this->kernel = $kernel;
    }

    /**
     * @Then /^article should be saved$/
     */
    public function errorShouldBeLogged()
    {
        // access the kernel in your steps
        $doctrine = $this->kernel->getContainer()->get('doctrine');
    }
}
